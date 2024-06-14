package com.java.recruitment.service.impl;

import com.java.recruitment.aspect.log.LogError;
import com.java.recruitment.repositoty.CandidateRepository;
import com.java.recruitment.repositoty.JobRequestRepository;
import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.repositoty.VacancyRepository;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.filter.GenericSpecification;
import com.java.recruitment.service.model.candidate.Candidate;
import com.java.recruitment.service.model.jobRequest.JobRequest;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.service.model.vacancy.Vacancy;
import com.java.recruitment.web.dto.hiring.ChangeJobRequestStatusDTO;
import com.java.recruitment.web.dto.hiring.JobRequestDTO;
import com.java.recruitment.web.dto.hiring.JobResponseDTO;
import com.java.recruitment.web.dto.mail.MailRequest;
import com.java.recruitment.web.mapper.JobRequestMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.java.recruitment.service.model.jobRequest.Status.NEW;

@Service
@RequiredArgsConstructor
@LogError
public class JobRequestService implements IJobRequestService {

    private final JobRequestMapper jobRequestMapper;

    private final JobRequestRepository jobRequestRepository;

    private final UserRepository userRepository;

    private final VacancyRepository vacancyRepository;

    private final CandidateRepository candidateRepository;

    private final FileService fileService;

    @Override
    @Transactional
    public JobResponseDTO createJobRequest(JobRequestDTO jobRequestDto, Long hrId) {

        Candidate candidate = candidateRepository.findById(jobRequestDto.getCandidateId())
                .orElseThrow(() -> new DataNotFoundException("Кандидат не найден"));

        User hr = userRepository.findById(hrId)
                .orElseThrow(() -> new DataNotFoundException("HR не найден"));

        Vacancy vacancy = vacancyRepository.findById(jobRequestDto.getVacancy_id())
                .orElseThrow(() -> new DataNotFoundException("Вакансия не найдена"));

        User recruiter = userRepository.findById(hrId)
                .orElseThrow(() -> new DataNotFoundException("Рекрутер не найден"));

        MultipartFile[] files = jobRequestDto.getFiles();

        List<String> filesNames = new ArrayList<>();

        JobRequest jobRequest = JobRequest.builder()
                .status(NEW)
                .candidate(candidate)
                .vacancy(vacancy)
                .hr(hr)
                .build();

        if (files != null) {
            for (MultipartFile file : files) {
                filesNames.add(fileService.upload(file));
            }
        }

        jobRequest.setFiles(filesNames);

        if (jobRequestDto.getDescription() != null) {
            jobRequest.setDescription(jobRequestDto.getDescription());
        }

        JobRequest savedJobRequest = jobRequestRepository.save(jobRequest);

        JobResponseDTO jobResponseDTO = jobRequestMapper.toDto(savedJobRequest);

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setProtocol("smtp");
        javaMailSender.setHost("smtp." + getDomainFromEmail(hr.getUsername()));
        javaMailSender.setPort(587);
        javaMailSender.setUsername(recruiter.getUsername());
        javaMailSender.setPassword(hr.getPassword());
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.starttls.enable", "true");
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.auth", "true");

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper
                    (
                            mimeMessage,
                            false,
                            StandardCharsets.UTF_8.name()
                    );

//            prepareMailMessage(mimeMessageHelper, hr, recruiter, mailRequest);
//
//            javaMailSender.send(mimeMessage);
//
//            MailResponse mailResponse = mailMapper.toResponse(mailRequest);
//            mailResponse.setSentDate(LocalDateTime.now());

        } catch (MessagingException e) {
            throw new MailSendException("Ошибка отправки письма", e);
        }
//        catch (IOException e) {
//            throw new DataProcessingException("Ошибка обработки файлов");
//        }

        return jobRequestMapper.toDto(savedJobRequest);
    }

    @Override
    public JobResponseDTO getJobRequestById(Long id) {
        JobRequest jobRequest = jobRequestRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Заявка не найдена"));
        return jobRequestMapper.toDto(jobRequest);
    }

    @Override
    @Transactional
    public JobResponseDTO updateJobRequest(ChangeJobRequestStatusDTO jobRequestDto) {
        JobRequest jobRequest = jobRequestRepository.findById(jobRequestDto.getId())
                .orElseThrow(() -> new DataNotFoundException("Заявка не найдена"));
        jobRequest.setStatus(jobRequestDto.getStatus());
        jobRequestRepository.save(jobRequest);
        return jobRequestMapper.toDto(jobRequest);
    }

    @Override
    @Transactional
    public void deleteJobRequest(Long id) {
        JobRequest jobRequest = jobRequestRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Заявка не найдена"));
        jobRequestRepository.delete(jobRequest);
    }

    @Override
    public Page<JobResponseDTO> getAllJobRequests(
            List<CriteriaModel> criteriaModelList,
            Long recruiter_id,
            Pageable pageable
    ) {
        Specification<JobRequest> specification
                = new GenericSpecification<>(criteriaModelList, JobRequest.class);
        Page<JobRequest> rooms = jobRequestRepository.findJobRequestsForRecruiter(
                recruiter_id,
                pageable,
                specification
        );
        return rooms.map(jobRequestMapper::toDto);
    }

    @Override
    public boolean isJobRequestOwner(
            final Long userId,
            final Long job_request_id
    ) {
        return jobRequestRepository.isJobRequestOwner(userId, job_request_id);
    }

    @Override
    public boolean isRecruiterForJobRequest(
            final Long userId,
            final Long job_request_id) {
        return jobRequestRepository.isRecruiterForJobRequest(userId, job_request_id);
    }

    private void prepareMailMessage(
            MimeMessageHelper mimeMessageHelper,
            User sender,
            User receiver,
            MailRequest mailRequest
    ) throws MessagingException, IOException {
        mimeMessageHelper.setFrom(sender.getUsername());
        mimeMessageHelper.setTo(receiver.getUsername());
        mimeMessageHelper.setSubject(mailRequest.getSubject());
        if (mailRequest.getText() != null) {
            mimeMessageHelper.setText(mailRequest.getText());
        }

        if (mailRequest.getFiles() != null) {
            addAttachments(mimeMessageHelper, mailRequest.getFiles());
        }
    }

    private void addAttachments(
            MimeMessageHelper mimeMessageHelper,
            MultipartFile[] files
    ) throws MessagingException, IOException {
        for (MultipartFile file : files) {
            if (file != null && file.getOriginalFilename() != null) {
                mimeMessageHelper.addAttachment(
                        file.getOriginalFilename(),
                        new ByteArrayResource(file.getBytes())
                );
            }
        }
    }

    private String getDomainFromEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex != -1) {
            return email.substring(atIndex + 1);
        } else {
            return "";
        }
    }
}

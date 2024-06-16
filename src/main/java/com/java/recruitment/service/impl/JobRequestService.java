package com.java.recruitment.service.impl;

import com.java.recruitment.aspect.log.LogError;
import com.java.recruitment.repositoty.CandidateRepository;
import com.java.recruitment.repositoty.JobRequestRepository;
import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.repositoty.VacancyRepository;
import com.java.recruitment.repositoty.exception.DataAccessException;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.IFileService;
import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.service.INotificationService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.filter.GenericSpecification;
import com.java.recruitment.service.model.candidate.Candidate;
import com.java.recruitment.service.model.jobRequest.JobRequest;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.service.model.vacancy.Vacancy;
import com.java.recruitment.web.dto.jobRequest.ChangeJobRequestStatusDTO;
import com.java.recruitment.web.dto.jobRequest.JobRequestDTO;
import com.java.recruitment.web.dto.jobRequest.JobResponseDTO;
import com.java.recruitment.web.mapper.JobRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.java.recruitment.service.model.chat.NotificationType.NEW_JOB_REQUEST;
import static com.java.recruitment.service.model.jobRequest.Status.NEW;

@Service
@RequiredArgsConstructor
@LogError
@Transactional(readOnly = true)
public class JobRequestService implements IJobRequestService {

    private final JobRequestMapper jobRequestMapper;

    private final JobRequestRepository jobRequestRepository;

    private final UserRepository userRepository;

    private final VacancyRepository vacancyRepository;

    private final CandidateRepository candidateRepository;

    private final IFileService fileService;

    private final INotificationService notificationService;

    @Override
    @Transactional
    public JobResponseDTO createJobRequest(
            final JobRequestDTO jobRequestDto,
            final Long hrId) {

        Vacancy vacancy = vacancyRepository.findById(jobRequestDto.getVacancyId())
                .orElseThrow(() -> new DataNotFoundException("Вакансия не найдена"));

        if (!vacancy.isActive()) {
            throw new DataAccessException("Вакансия закрыта");
        }

        Candidate candidate = candidateRepository.findById(jobRequestDto.getCandidateId())
                .orElseThrow(() -> new DataNotFoundException("Кандидат не найден"));

        User hr = userRepository.findById(hrId)
                .orElseThrow(() -> new DataNotFoundException("HR не найден"));

        User recruiter = userRepository.findById(vacancy.getRecruiter().getId())
                .orElseThrow(() -> new DataNotFoundException("Рекрутер не найден"));

        MultipartFile[] files = jobRequestDto.getFiles();

        List<String> filesNames = new ArrayList<>();

        JobRequest jobRequest = JobRequest.builder()
                .status(NEW)
                .hr(hr)
                .candidate(candidate)
                .vacancy(vacancy)
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

        Properties properties = new Properties();
        properties.setProperty("vacancy.title", vacancy.getTitle());
        properties.setProperty("vacancy.position", vacancy.getPosition());
        notificationService.sendNotification(
                recruiter,
                NEW_JOB_REQUEST,
                properties
        );

        return jobRequestMapper.toDto(savedJobRequest);
    }

    @Override
    public JobResponseDTO getJobRequestById(final Long id) {
        JobRequest jobRequest = jobRequestRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Заявка не найдена"));
        return jobRequestMapper.toDto(jobRequest);
    }

    @Override
    @Transactional
    public JobResponseDTO updateJobRequest(final ChangeJobRequestStatusDTO jobRequestDto) {
        JobRequest jobRequest = jobRequestRepository.findById(jobRequestDto.getId())
                .orElseThrow(() -> new DataNotFoundException("Заявка не найдена"));
        jobRequest.setStatus(jobRequestDto.getStatus());
        jobRequestRepository.save(jobRequest);
        return jobRequestMapper.toDto(jobRequest);
    }

    @Override
    @Transactional
    public void deleteJobRequest(final Long id) {

        JobRequest jobRequest = jobRequestRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Заявка не найдена"));

        fileService.delete(jobRequest.getFiles());

        jobRequestRepository.delete(jobRequest);
    }

    @Override
    public Page<JobResponseDTO> getAllJobRequests(
            final List<CriteriaModel> criteriaModelList,
            final Long recruiter_id,
            Pageable pageable
    ) {
        Specification<JobRequest> specification
                = new GenericSpecification<>(criteriaModelList, JobRequest.class);
        Page<JobRequest> jobRequests = jobRequestRepository.findAllJobRequestsByRecruiterIdAndCriteria(
                recruiter_id,
                specification,
                pageable
        );
        return jobRequests.map(jobRequestMapper::toDto);
    }

    @Override
    public boolean isJobRequestOwner(
            final Long userId,
            final Long job_request_id
    ) {
        return jobRequestRepository.isJobRequestOwner(userId, job_request_id);
    }

    @Override
    public boolean isJobRequestConsumer(
            final Long userId,
            final Long job_request_id) {
        return jobRequestRepository.isJobRequestConsumer(userId, job_request_id);
    }
}

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
import com.java.recruitment.service.filter.JoinType;
import com.java.recruitment.service.model.candidate.Candidate;
import com.java.recruitment.service.model.jobRequest.JobRequest;
import com.java.recruitment.service.model.jobRequest.JobRequest_;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.service.model.user.User_;
import com.java.recruitment.service.model.vacancy.Vacancy;
import com.java.recruitment.util.FilterParser;
import com.java.recruitment.web.dto.jobRequest.ChangeJobRequestStatusDTO;
import com.java.recruitment.web.dto.jobRequest.JobRequestDTO;
import com.java.recruitment.web.dto.jobRequest.JobResponseDTO;
import com.java.recruitment.web.mapper.JobRequestMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

    private final IFileService fileService;

    private final INotificationService notificationService;

    private final JobRequestRepository jobRequestRepository;

    private final UserRepository userRepository;

    private final VacancyRepository vacancyRepository;

    private final CandidateRepository candidateRepository;

    private final JobRequestMapper jobRequestMapper;

    private final EntityManager entityManager;

    @Override
    @Transactional
    public JobResponseDTO createJobRequest(
            final Long userId,
            final JobRequestDTO jobRequestDto
    ) {

        Vacancy vacancy = vacancyRepository.findById(jobRequestDto.getVacancyId())
                .orElseThrow(() -> new DataNotFoundException("Вакансия не найдена"));

        if (!vacancy.isActive()) {
            throw new DataAccessException("Вакансия закрыта");
        }

        Candidate candidate = candidateRepository.findById(jobRequestDto.getCandidateId())
                .orElseThrow(() -> new DataNotFoundException("Кандидат не найден"));

        User hr = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("HR не найден"));

        User recruiter = userRepository.findById(vacancy.getRecruiter().getId())
                .orElseThrow(() -> new DataNotFoundException("Рекрутер не найден"));

        MultipartFile[] files = jobRequestDto.getFiles();

        List<String> filesNames = new ArrayList<>();

        JobRequest jobRequest = JobRequest.builder()
                .status(NEW)
                .hr(hr)
                .candidate(candidate)
                .recruiter(recruiter)
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
    public JobResponseDTO getJobRequestById(
            final Long userId,
            final Long jobRequestId
    ) {
        JobRequest jobRequest = findJobRequest(
                userId,
                jobRequestId
        );
        return jobRequestMapper.toDto(jobRequest);
    }

    @Override
    @Transactional
    public JobResponseDTO updateJobRequest(
            final Long userId,
            final ChangeJobRequestStatusDTO dto
    ) {
        JobRequest jobRequest = findJobRequest(
                userId,
                dto.getId()
        );
        jobRequest.setStatus(dto.getStatus());
        jobRequestRepository.save(jobRequest);
        return jobRequestMapper.toDto(jobRequest);
    }

    @Override
    @Transactional
    public void deleteJobRequest(
            final Long userId,
            final Long jobRequestId
    ) {

        JobRequest jobRequest = findJobRequest(
                userId,
                jobRequestId
        );

        fileService.delete(jobRequest.getFiles());

        jobRequestRepository.delete(jobRequest);
    }

    @SneakyThrows
    @Override
    public Page<JobResponseDTO> getFilteredJobRequests(
            final Long userId,
            final String criteriaJson,
            final JoinType joinType,
            final Pageable pageable
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));

        List<CriteriaModel> criteriaList = List.of();

        if (criteriaJson != null) {
            criteriaList = FilterParser.parseCriteriaJson(criteriaJson);
        }

        switch (user.getRole()) {
            case HR:

                Specification<JobRequest> hrSp = (root, query, cb) ->
                        cb.equal(root.get(JobRequest_.hr).get(User_.id), user.getId());

                if (!criteriaList.isEmpty()) {
                    return jobRequestRepository.findAll(
                            new GenericSpecification<>(
                                    criteriaList,
                                    joinType,
                                    JobRequest.class
                            )
                                    .and(hrSp),
                            pageable
                    ).map(jobRequestMapper::toDto);
                } else {
                    return jobRequestRepository.findAll(hrSp, pageable)
                            .map(jobRequestMapper::toDto);
                }

            case RECRUITER:

                Specification<JobRequest> recruiterSp = (root, query, cb) ->
                        cb.equal(root.get(JobRequest_.recruiter).get(User_.ID), user.getId());

                if (!criteriaList.isEmpty()) {
                    return jobRequestRepository.findAll(
                            new GenericSpecification<>(
                                    criteriaList,
                                    joinType,
                                    JobRequest.class
                            )
                                    .and(recruiterSp),
                            pageable
                    ).map(jobRequestMapper::toDto);
                } else {
                    return jobRequestRepository.findAll(recruiterSp, pageable)
                            .map(jobRequestMapper::toDto);
                }

            case ADMIN:

                if (!criteriaList.isEmpty()) {
                    return jobRequestRepository.findAll(
                            new GenericSpecification<>(
                                    criteriaList,
                                    joinType,
                                    JobRequest.class
                            ),
                            pageable
                    ).map(jobRequestMapper::toDto);
                } else {
                    return jobRequestRepository.findAll(pageable)
                            .map(jobRequestMapper::toDto);
                }

            default:
                return Page.empty(pageable);

        }
    }

    @Override
    @Transactional
    public String downloadByJobRequestId(
            final Long userId,
            final Long jobRequestId
    ) {

        JobRequest jobRequest = findJobRequest(
                userId,
                jobRequestId
        );

        return fileService.download(jobRequest);
    }

    private JobRequest findJobRequest(
            final Long userId,
            final Long jobRequestId
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));
        CriteriaBuilder cb =
                entityManager.getCriteriaBuilder();
        CriteriaQuery<JobRequest> query =
                cb.createQuery(JobRequest.class);
        Root<JobRequest> root =
                query.from(JobRequest.class);
        Predicate jobRequestPredicate =
                cb.equal(root.get(JobRequest_.id), jobRequestId);

        switch (user.getRole()) {
            case ADMIN:
                query.where(jobRequestPredicate);
                try {
                    return entityManager.createQuery(query).getSingleResult();
                } catch (NoResultException e) {
                    throw new DataNotFoundException("Заявка не найдена");
                }
            case HR:
                Predicate hrPredicate = cb.equal(root.get(JobRequest_.hr).get(User_.id), userId);
                Predicate finalHrPredicate = cb.and(hrPredicate, jobRequestPredicate);
                query.where(finalHrPredicate);
                try {
                    return entityManager.createQuery(query).getSingleResult();
                } catch (NoResultException e) {
                    throw new DataNotFoundException("Заявка не найдена");
                }
            case RECRUITER:
                Predicate recruiterPredicate = cb.equal(root.get(JobRequest_.recruiter).get(User_.id), userId);
                Predicate finalRecruiterPredicate = cb.and(recruiterPredicate, jobRequestPredicate);
                query.where(finalRecruiterPredicate);
                try {
                    return entityManager.createQuery(query).getSingleResult();
                } catch (NoResultException e) {
                    throw new DataNotFoundException("Заявка не найдена");
                }
            default:
                throw new DataNotFoundException("Роль пользователя не поддерживается");
        }
    }

}

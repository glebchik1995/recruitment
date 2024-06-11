package com.java.recruitment.service.impl;

import com.java.recruitment.repositoty.CandidateRepository;
import com.java.recruitment.repositoty.JobRequestRepository;
import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.filter.GenericSpecification;
import com.java.recruitment.service.model.candidate.Candidate;
import com.java.recruitment.service.model.hiring.JobRequest;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.hiring.ChangeJobRequestStatusDTO;
import com.java.recruitment.web.dto.hiring.JobRequestDTO;
import com.java.recruitment.web.dto.hiring.JobResponseDTO;
import com.java.recruitment.web.mapper.CandidateMapper;
import com.java.recruitment.web.mapper.JobRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.java.recruitment.service.model.hiring.Status.NEW;

@Service
@RequiredArgsConstructor
@Slf4j
class JobRequestService implements IJobRequestService {

    private final JobRequestMapper jobRequestMapper;

    private final CandidateMapper candidateMapper;

    private final JobRequestRepository jobRequestRepository;

    private final UserRepository userRepository;

    private final CandidateRepository candidateRepository;

    private final FileService fileService;

    @Override
    @Transactional
    public JobResponseDTO createJobRequest(JobRequestDTO jobRequestDto) {
        Candidate candidate = candidateMapper.toEntity(jobRequestDto.getCandidate());
        candidateRepository.save(candidate);

        User hr = userRepository.findById(jobRequestDto.getHrId())
                .orElseThrow(() -> new DataNotFoundException("HR не найден"));

        MultipartFile[] files = jobRequestDto.getFiles();

        List<String> filesNames = new ArrayList<>();

        JobRequest jobRequest = JobRequest.builder()
                .status(NEW)
                .candidate(candidate)
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
        log.info("Создана новая заявка на работу с ID: {}", savedJobRequest.getId());

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
    public Page<JobResponseDTO> getAllJobRequests(List<CriteriaModel> criteriaModelList, Pageable pageable) {
        Specification<JobRequest> specification
                = new GenericSpecification<>(criteriaModelList, JobRequest.class);
        Page<JobRequest> rooms = jobRequestRepository.findAll(specification, pageable);
        return rooms.map(jobRequestMapper::toDto);
    }
}

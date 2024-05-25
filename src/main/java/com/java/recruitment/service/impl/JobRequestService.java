package com.java.recruitment.service.impl;

import com.java.recruitment.repositoty.HrRepository;
import com.java.recruitment.repositoty.JobRequestRepository;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.model.candidate.Candidate;
import com.java.recruitment.service.model.hiring.JobRequest;
import com.java.recruitment.service.model.hr.HR;
import com.java.recruitment.web.dto.hiring.ChangeJobRequestStatusDTO;
import com.java.recruitment.web.dto.hiring.JobRequestDTO;
import com.java.recruitment.web.dto.hiring.JobRequestFileDTO;
import com.java.recruitment.web.mapper.impl.CandidateMapper;
import com.java.recruitment.web.mapper.impl.JobRequestFileMapper;
import com.java.recruitment.web.mapper.impl.JobRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.java.recruitment.service.model.hiring.Status.NEW;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobRequestService implements IJobRequestService {

    private final JobRequestFileMapper jobRequestFileMapper;

    private final JobRequestMapper jobRequestMapper;

    private final CandidateMapper candidateMapper;

    private final JobRequestRepository jobRequestRepository;

    private final HrRepository hrRepository;

    private final FileService fileService;

    @Override
    @Transactional
    public JobRequestDTO createJobRequest(JobRequestDTO jobRequestDto) {
        Candidate candidate = candidateMapper.toEntity(jobRequestDto.getCandidate());
        HR hr = hrRepository.findById(jobRequestDto.getHrId())
                .orElseThrow(() -> new DataNotFoundException("HR не найден"));

        JobRequest jobRequest = JobRequest.builder()
                .status(NEW)
                .candidate(candidate)
                .hr(hr)
                .build();

        List<JobRequestFileDTO> jobRequestFiles = jobRequestDto.getFiles().stream()
                .map(fileService::upload)
                .toList();

        if (!jobRequestFiles.isEmpty()) {
            jobRequest.setFiles(jobRequestFiles.stream()
                    .map(jobRequestFileMapper::toEntity)
                    .collect(Collectors.toList()));
        }

        if (!jobRequestDto.getDescription().isEmpty()) {
            jobRequest.setDescription(jobRequestDto.getDescription());
        }

        JobRequest savedJobRequest = jobRequestRepository.save(jobRequest);
        log.info("Создана новая заявка на работу с ID: {}", savedJobRequest.getId());

        return jobRequestMapper.toDto(savedJobRequest);
    }

    @Override
    public JobRequest getJobRequestById(Long id) {
        return null;
    }

    @Override
    public JobRequest updateJobRequest(Long id, ChangeJobRequestStatusDTO jobRequestDto) {
        return null;
    }

    @Override
    public void deleteJobRequest(Long id) {

    }

    @Override
    public Page<JobRequestDTO> getAllJobRequests(CriteriaModel model, Pageable pageable) {
        return null;
    }
}

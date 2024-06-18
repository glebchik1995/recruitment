package com.java.recruitment.service;

import com.java.recruitment.web.dto.jobRequest.ChangeJobRequestStatusDTO;
import com.java.recruitment.web.dto.jobRequest.JobRequestDTO;
import com.java.recruitment.web.dto.jobRequest.JobResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IJobRequestService {

    JobResponseDTO createJobRequest(JobRequestDTO jobRequestDto, Long hrId);

    JobResponseDTO getJobRequestById(Long userId, Long jobRequestId);

    JobResponseDTO updateJobRequest(ChangeJobRequestStatusDTO jobRequestDto);

    void deleteJobRequest(Long id);

    boolean isJobRequestOwner(
            Long userId,
            Long job_request_id
    );

    boolean isJobRequestConsumer(
            Long userId,
            Long job_request_id
    );

    Page<JobResponseDTO> getFilteredJobRequests(
            Long userId,
            String criteriaJson,
            Pageable pageable
    );
}

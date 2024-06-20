package com.java.recruitment.service;

import com.java.recruitment.service.filter.JoinType;
import com.java.recruitment.web.dto.jobRequest.ChangeJobRequestStatusDTO;
import com.java.recruitment.web.dto.jobRequest.JobRequestDTO;
import com.java.recruitment.web.dto.jobRequest.JobResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IJobRequestService {

    JobResponseDTO createJobRequest(Long userId, JobRequestDTO dto);

    JobResponseDTO getJobRequestById(Long userId, Long jobRequestId);

    JobResponseDTO updateJobRequest(Long userId, ChangeJobRequestStatusDTO dto);

    void deleteJobRequest(Long userId, Long jobRequestId);

    Page<JobResponseDTO> getFilteredJobRequests(
            Long userId,
            String criteriaJson,
            JoinType joinType,
            Pageable pageable
    );

    String downloadByJobRequestId(Long userId, Long jobRequestId);
}

package com.java.recruitment.service;

import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.web.dto.jobRequest.ChangeJobRequestStatusDTO;
import com.java.recruitment.web.dto.jobRequest.JobRequestDTO;
import com.java.recruitment.web.dto.jobRequest.JobResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IJobRequestService {

    JobResponseDTO createJobRequest(JobRequestDTO jobRequestDto, Long hrId);

    JobResponseDTO getJobRequestById(Long id);

    JobResponseDTO updateJobRequest(ChangeJobRequestStatusDTO jobRequestDto);

    void deleteJobRequest(Long id);

    Page<JobResponseDTO> getAllJobRequests(
            List<CriteriaModel> criteriaModelList,
            Long recruiter_id,
            Pageable pageable
    );

    boolean isJobRequestOwner(
            Long userId,
            Long job_request_id
    );

    boolean isRecruiterForJobRequest(
            Long userId,
            Long job_request_id
    );
}

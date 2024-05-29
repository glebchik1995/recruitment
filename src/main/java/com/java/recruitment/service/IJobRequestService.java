package com.java.recruitment.service;

import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.model.hiring.JobRequest;
import com.java.recruitment.web.dto.hiring.ChangeJobRequestStatusDTO;
import com.java.recruitment.web.dto.hiring.JobRequestDTO;
import com.java.recruitment.web.dto.hiring.JobResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IJobRequestService {

    JobResponseDTO createJobRequest(JobRequestDTO jobRequestDto);
    JobRequest getJobRequestById(Long id);
    JobRequest updateJobRequest(ChangeJobRequestStatusDTO jobRequestDto);
    void deleteJobRequest(Long id);
    Page<JobResponseDTO> getAllJobRequests(CriteriaModel model, Pageable pageable);
}

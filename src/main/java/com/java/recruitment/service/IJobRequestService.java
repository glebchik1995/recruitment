package com.java.recruitment.service;

import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.model.hiring.JobRequest;
import com.java.recruitment.web.dto.hiring.ChangeJobRequestStatusDTO;
import com.java.recruitment.web.dto.hiring.JobRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IJobRequestService {

    JobRequest createJobRequest(JobRequestDTO jobRequestDto);
    JobRequest getJobRequestById(Long id);
    JobRequest updateJobRequest(Long id, ChangeJobRequestStatusDTO jobRequestDto);
    void deleteJobRequest(Long id);
    Page<JobRequestDTO> getAllJobRequests(CriteriaModel model, Pageable pageable);
}

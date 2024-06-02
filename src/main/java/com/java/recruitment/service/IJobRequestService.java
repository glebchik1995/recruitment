package com.java.recruitment.service;

import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.web.dto.hiring.ChangeJobRequestStatusDTO;
import com.java.recruitment.web.dto.hiring.JobRequestDTO;
import com.java.recruitment.web.dto.hiring.JobResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IJobRequestService {

    JobResponseDTO createJobRequest(JobRequestDTO jobRequestDto);
    JobResponseDTO getJobRequestById(Long id);
    JobResponseDTO updateJobRequest(ChangeJobRequestStatusDTO jobRequestDto);
    void deleteJobRequest(Long id);
    Page<JobResponseDTO> getAllJobRequests(List<CriteriaModel> criteriaModelList, Pageable pageable);
}

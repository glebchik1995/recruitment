package com.java.recruitment.service.impl;

import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.model.hiring.JobRequest;
import com.java.recruitment.web.dto.hiring.ChangeJobRequestStatusDTO;
import com.java.recruitment.web.dto.hiring.JobRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class JobRequestService implements IJobRequestService {
    @Override
    public JobRequest createJobRequest(JobRequestDTO jobRequestDto) {
        return null;
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

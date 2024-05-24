package com.java.recruitment.service.impl;

import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.model.hiring.JobRequest;
import com.java.recruitment.web.dto.hiring.ChangeJobRequestStatusDTO;
import com.java.recruitment.web.dto.hiring.JobRequestDTO;
import com.java.recruitment.web.mapper.impl.JobRequestFileMapper;
import com.java.recruitment.web.mapper.impl.JobRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobRequestService implements IJobRequestService {

    private final JobRequestFileMapper jobRequestFileMapper;

    private final JobRequestMapper jobRequestMapper;


    @Override
    public JobRequest createJobRequest(JobRequestDTO jobRequestDto) {
        JobRequest jobRequest = jobRequestMapper.toEntity(jobRequestDto);

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

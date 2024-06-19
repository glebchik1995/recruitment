package com.java.recruitment.web.mapper;

import com.java.recruitment.service.model.jobRequest.JobRequest;
import com.java.recruitment.web.dto.jobRequest.JobResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {
                CandidateMapper.class,
                UserMapper.class
        })
public interface JobRequestMapper {

    JobResponseDTO toDto(JobRequest jobRequest);
}

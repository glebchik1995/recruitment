package com.java.recruitment.web.mapper;

import com.java.recruitment.service.model.jobRequest.JobRequest;
import com.java.recruitment.web.dto.hiring.JobResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CandidateMapper.class})
public interface JobRequestMapper {

    @Mapping(target = "hrId", source = "hr.id")
    JobResponseDTO toDto(JobRequest jobRequest);
}

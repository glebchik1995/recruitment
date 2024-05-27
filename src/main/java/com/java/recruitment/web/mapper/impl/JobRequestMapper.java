package com.java.recruitment.web.mapper.impl;

import com.java.recruitment.service.model.hiring.JobRequest;
import com.java.recruitment.web.dto.hiring.JobRequestDTO;
import com.java.recruitment.web.dto.hiring.JobResponseDTO;
import com.java.recruitment.web.mapper.Mappable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CandidateMapper.class})
public interface JobRequestMapper {

    @Mapping(target = "hrId", source = "hr.id")
    JobResponseDTO toDto(JobRequest jobRequest);
}

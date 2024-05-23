package com.java.recruitment.web.mapper.impl;

import com.java.recruitment.web.dto.hiring.JobRequestDTO;
import com.java.recruitment.web.mapper.Mappable;
import com.java.recruitment.service.model.hiring.JobRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobRequestMapper extends Mappable<JobRequest, JobRequestDTO> {
}

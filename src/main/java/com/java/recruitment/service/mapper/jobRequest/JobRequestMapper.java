package com.java.recruitment.service.mapper.jobRequest;

import com.java.recruitment.service.dto.hiring.JobRequestDTO;
import com.java.recruitment.service.mapper.Mappable;
import com.java.recruitment.service.model.hiring.JobRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobRequestMapper extends Mappable<JobRequest, JobRequestDTO> {
}

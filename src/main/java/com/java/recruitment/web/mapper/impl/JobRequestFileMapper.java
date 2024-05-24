package com.java.recruitment.web.mapper.impl;

import com.java.recruitment.service.model.hiring.JobRequestFile;
import com.java.recruitment.web.dto.hiring.JobRequestFileDTO;
import com.java.recruitment.web.mapper.Mappable;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobRequestFileMapper extends Mappable<JobRequestFile, JobRequestFileDTO>{

}

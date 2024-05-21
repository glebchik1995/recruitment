package com.java.recruitment.service.mapper.candidate;

import com.java.recruitment.service.dto.candidate.CandidateDTO;
import com.java.recruitment.service.mapper.Mappable;
import com.java.recruitment.service.model.candidate.Candidate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface  CandidateMapper extends Mappable<Candidate, CandidateDTO> {
}

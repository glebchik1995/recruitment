package com.java.recruitment.web.mapper;

import com.java.recruitment.web.dto.candidate.CandidateDTO;
import com.java.recruitment.service.model.candidate.Candidate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface  CandidateMapper extends Mappable<Candidate, CandidateDTO> {
}

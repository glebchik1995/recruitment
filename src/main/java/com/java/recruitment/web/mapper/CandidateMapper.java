package com.java.recruitment.web.mapper;

import com.java.recruitment.service.model.candidate.Candidate;
import com.java.recruitment.web.dto.candidate.RequestCandidateDTO;
import com.java.recruitment.web.dto.candidate.ResponseCandidateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface  CandidateMapper {

    @Mapping(target = "hr", ignore = true)
    @Mapping(target = "jobRequests", ignore = true)
    Candidate toEntity(RequestCandidateDTO candidateDTO);

    ResponseCandidateDTO toDto(Candidate candidate);
}

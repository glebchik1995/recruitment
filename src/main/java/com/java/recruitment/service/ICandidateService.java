package com.java.recruitment.service;

import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.web.dto.candidate.CandidateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICandidateService {

    CandidateDTO createCandidate(CandidateDTO candidate);

    CandidateDTO updateCandidate(CandidateDTO candidateDTO);

    Page<CandidateDTO> getAllCandidates(List<CriteriaModel> criteriaList, Pageable pageable);

    CandidateDTO getCandidateById(Long id);

    void deleteCandidate(Long id);
}

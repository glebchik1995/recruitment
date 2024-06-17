package com.java.recruitment.service;

import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.web.dto.candidate.RequestCandidateDTO;
import com.java.recruitment.web.dto.candidate.ResponseCandidateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICandidateService {

    ResponseCandidateDTO createCandidate(Long hrId, RequestCandidateDTO candidate);

    ResponseCandidateDTO updateCandidate(Long hrId, RequestCandidateDTO candidateDTO);

    Page<ResponseCandidateDTO> getFilteredCandidates(List<CriteriaModel> criteriaList, Pageable pageable);

    ResponseCandidateDTO getCandidateById(Long hrId, Long id);

    void deleteCandidate(Long hrId, Long id);
}

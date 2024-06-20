package com.java.recruitment.service;

import com.java.recruitment.service.filter.JoinType;
import com.java.recruitment.web.dto.candidate.RequestCandidateDTO;
import com.java.recruitment.web.dto.candidate.ResponseCandidateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICandidateService {

    ResponseCandidateDTO createCandidate(Long userId, RequestCandidateDTO candidate);

    ResponseCandidateDTO updateCandidate(Long userId, RequestCandidateDTO candidateDTO);

    Page<ResponseCandidateDTO> getFilteredCandidates(
            Long userId,
            String criteriaJson,
            JoinType joinType,
            Pageable pageable
    );

    ResponseCandidateDTO getCandidateById(Long userId, Long id);

    void deleteCandidate(Long userId, Long id);
}

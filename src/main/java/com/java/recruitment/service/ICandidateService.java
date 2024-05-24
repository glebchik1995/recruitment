package com.java.recruitment.service;

import com.java.recruitment.web.dto.candidate.CandidateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICandidateService {

        CandidateDTO createCandidate(CandidateDTO candidateDTO);
        Page<CandidateDTO> getAllCandidates (Pageable pageable);
        CandidateDTO getCandidateById (Long id);
        CandidateDTO updateCandidate (Long id, CandidateDTO candidateDTO);
        void deleteCandidate (Long id);
}

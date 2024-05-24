package com.java.recruitment.service.impl;

import com.java.recruitment.service.ICandidateService;
import com.java.recruitment.web.dto.candidate.CandidateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CandidateService implements ICandidateService {
    @Override
    public CandidateDTO createCandidate(CandidateDTO candidateDTO) {
        return null;
    }

    @Override
    public Page<CandidateDTO> getAllCandidates(Pageable pageable) {
        return null;
    }

    @Override
    public CandidateDTO getCandidateById(Long id) {
        return null;
    }

    @Override
    public CandidateDTO updateCandidate(Long id, CandidateDTO candidateDTO) {
        return null;
    }

    @Override
    public void deleteCandidate(Long id) {

    }
}

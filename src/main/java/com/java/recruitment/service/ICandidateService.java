package com.java.recruitment.service;

import com.java.recruitment.web.dto.candidate.CandidateDTO;

public interface ICandidateService {

    CandidateDTO createCandidate(CandidateDTO candidate);

    CandidateDTO updateCandidate(CandidateDTO candidateDTO);

    void deleteCandidate(Long id);
}

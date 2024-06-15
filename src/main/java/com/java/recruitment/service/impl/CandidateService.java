package com.java.recruitment.service.impl;

import com.java.recruitment.aspect.log.LogError;
import com.java.recruitment.repositoty.CandidateRepository;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.ICandidateService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.filter.GenericSpecification;
import com.java.recruitment.service.model.candidate.Candidate;
import com.java.recruitment.util.NullPropertyCopyHelper;
import com.java.recruitment.web.dto.candidate.CandidateDTO;
import com.java.recruitment.web.mapper.CandidateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@LogError
@RequiredArgsConstructor
public class CandidateService implements ICandidateService {

    private final CandidateMapper candidateMapper;

    private final CandidateRepository candidateRepository;

    @Override
    public CandidateDTO createCandidate(CandidateDTO candidateDTO) {
        return candidateMapper.toDto(
                candidateRepository.save(
                        candidateMapper.toEntity(
                                candidateDTO
                        )
                )
        );
    }

    @Override
    public CandidateDTO updateCandidate(CandidateDTO candidateDTO) {
        Candidate candidate = candidateRepository.findById(candidateDTO.getId())
                .orElseThrow(() -> new DataNotFoundException("Кандидат не найден"));
        NullPropertyCopyHelper.copyNonNullProperties(candidateDTO, candidate);
        Candidate updatedCandidate = candidateRepository.save(candidate);
        return candidateMapper.toDto(updatedCandidate);
    }

    @Override
    public Page<CandidateDTO> getAllCandidates
            (
                    List<CriteriaModel> criteriaList,
                    Pageable pageable
            ) {


        Specification<Candidate> specification
                = new GenericSpecification<>(criteriaList, Candidate.class);
        Page<Candidate> candidates = candidateRepository.findAll(
                specification,
                pageable
        );
        return candidates.map(candidateMapper::toDto);
    }

    @Override
    public CandidateDTO getCandidateById(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Кандидат не найден"));
        return candidateMapper.toDto(candidate);
    }

    @Override
    public void deleteCandidate(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Кандидат не найден"));
        candidateRepository.delete(candidate);
    }
}

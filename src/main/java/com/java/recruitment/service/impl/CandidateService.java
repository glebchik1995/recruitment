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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@LogError
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CandidateService implements ICandidateService {

    private final CandidateMapper candidateMapper;

    private final CandidateRepository candidateRepository;

    @Override
    @Transactional
    public CandidateDTO createCandidate(final CandidateDTO candidateDTO) {
        return candidateMapper.toDto(
                candidateRepository.save(
                        candidateMapper.toEntity(
                                candidateDTO
                        )
                )
        );
    }

    @Override
    @Transactional
    public CandidateDTO updateCandidate(final CandidateDTO candidateDTO) {
        Candidate candidate = candidateRepository.findById(candidateDTO.getId())
                .orElseThrow(() -> new DataNotFoundException("Кандидат не найден"));
        NullPropertyCopyHelper.copyNonNullProperties(candidateDTO, candidate);
        Candidate updatedCandidate = candidateRepository.save(candidate);
        return candidateMapper.toDto(updatedCandidate);
    }

    @Override
    public Page<CandidateDTO> getAllCandidates
            (
                    final List<CriteriaModel> criteriaList,
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
    public CandidateDTO getCandidateById(final Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Кандидат не найден"));
        return candidateMapper.toDto(candidate);
    }

    @Override
    @Transactional
    public void deleteCandidate(final Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Кандидат не найден"));
        candidateRepository.delete(candidate);
    }
}

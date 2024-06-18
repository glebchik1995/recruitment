package com.java.recruitment.service.impl;

import com.java.recruitment.aspect.log.LogError;
import com.java.recruitment.repositoty.CandidateRepository;
import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.ICandidateService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.filter.GenericSpecification;
import com.java.recruitment.service.filter.JoinType;
import com.java.recruitment.service.model.candidate.Candidate;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.util.AccessChecker;
import com.java.recruitment.util.FilterParser;
import com.java.recruitment.util.NullPropertyCopyHelper;
import com.java.recruitment.web.dto.candidate.RequestCandidateDTO;
import com.java.recruitment.web.dto.candidate.ResponseCandidateDTO;
import com.java.recruitment.web.mapper.CandidateMapper;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.java.recruitment.service.filter.Operation.EQUALS;

@Service
@LogError
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CandidateService implements ICandidateService {

    private final CandidateMapper candidateMapper;

    private final UserRepository userRepository;

    private final CandidateRepository candidateRepository;

    @Override
    @Transactional
    public ResponseCandidateDTO createCandidate(
            final Long hrId,
            final RequestCandidateDTO candidateDTO
    ) {
        Candidate candidate
                = candidateMapper.toEntity(candidateDTO);
        User user = userRepository.findById(hrId)
                .orElseThrow(() -> new DataNotFoundException(
                                "Пользователь не найден"
                        )
                );
        candidate.setHr(user);
        return candidateMapper.toDto(candidateRepository.save(candidate));
    }

    @Override
    @Transactional
    public ResponseCandidateDTO updateCandidate(
            final Long hrId,
            final RequestCandidateDTO candidateDTO
    ) {
        Candidate candidate = candidateRepository.findById(candidateDTO.getId())
                .orElseThrow(() -> new DataNotFoundException("Кандидат не найден"));
        AccessChecker.checkAccess(
                candidate.getHr().getId(),
                hrId
        );
        NullPropertyCopyHelper.copyNonNullProperties(candidateDTO, candidate);
        Candidate updatedCandidate = candidateRepository.save(candidate);
        return candidateMapper.toDto(updatedCandidate);

    }

    @Override
    public Page<ResponseCandidateDTO> getFilteredCandidates(
            final Long userId,
            final String criteriaJson,
            final Pageable pageable
    ) {

        List<CriteriaModel> criteriaList = new ArrayList<>();

        CriteriaModel model = CriteriaModel.builder()
                .field("hr.id")
                .operation(EQUALS)
                .value(userId)
                .joinType(JoinType.AND)
                .build();

        criteriaList.add(model);

        if (criteriaJson != null) {
            List<CriteriaModel> parsedCriteria;
            try {
                parsedCriteria = FilterParser.parseCriteriaJson(criteriaJson);
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
            criteriaList.addAll(parsedCriteria);
        }
        Specification<Candidate> sp
                = new GenericSpecification<>(criteriaList, Candidate.class);

        Page<Candidate> candidates = candidateRepository.findAll(
                sp,
                pageable
        );

        return candidates.map(candidateMapper::toDto);

    }

    @Override
    public ResponseCandidateDTO getCandidateById(
            final Long hrId,
            final Long candidateId
    ) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new DataNotFoundException("Кандидат не найден"));
        AccessChecker.checkAccess(
                candidate.getHr().getId(),
                hrId
        );
        return candidateMapper.toDto(candidate);
    }

    @Override
    @Transactional
    public void deleteCandidate(
            final Long hrId,
            final Long id
    ) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Кандидат не найден"));
        AccessChecker.checkAccess(
                candidate.getHr().getId(),
                hrId
        );
        candidateRepository.delete(candidate);
    }
}

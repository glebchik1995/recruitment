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
import com.java.recruitment.service.model.candidate.Candidate_;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.service.model.user.User_;
import com.java.recruitment.util.FilterParser;
import com.java.recruitment.util.NullPropertyCopyHelper;
import com.java.recruitment.web.dto.candidate.RequestCandidateDTO;
import com.java.recruitment.web.dto.candidate.ResponseCandidateDTO;
import com.java.recruitment.web.mapper.CandidateMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.java.recruitment.service.model.user.Role.ADMIN;

@Service
@LogError
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CandidateService implements ICandidateService {

    private final CandidateMapper candidateMapper;

    private final UserRepository userRepository;

    private final CandidateRepository candidateRepository;

    private final EntityManager entityManager;

    @Override
    @Transactional
    public ResponseCandidateDTO createCandidate(
            final Long userId,
            final RequestCandidateDTO candidateDTO
    ) {
        Candidate candidate
                = candidateMapper.toEntity(candidateDTO);
        User user = userRepository.findById(userId)
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
            final Long userId,
            final RequestCandidateDTO candidateDTO
    ) {
        Candidate candidate = findCandidate(
                userId,
                candidateDTO.getId()
        );
        NullPropertyCopyHelper.copyNonNullProperties(
                        candidateDTO,
                        candidate
                );

        Candidate updatedCandidate =
                candidateRepository.save(candidate);
        return candidateMapper.toDto(updatedCandidate);
    }

    @SneakyThrows
    @Override
    public Page<ResponseCandidateDTO> getFilteredCandidates(
            final Long userId,
            final String criteriaJson,
            final JoinType joinType,
            final Pageable pageable
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));

        List<CriteriaModel> criteriaList = List.of();

        if (criteriaJson != null) {
            criteriaList = FilterParser.parseCriteriaJson(criteriaJson);
        }

        switch (user.getRole()) {
            case HR:

                Specification<Candidate> hrSp = (root, query, cb) ->
                        cb.equal(root.get(Candidate_.hr).get(User_.id), user.getId());

                if (!criteriaList.isEmpty()) {
                    return candidateRepository.findAll(
                            new GenericSpecification<>(
                                    criteriaList,
                                    joinType,
                                    Candidate.class
                            ).and(hrSp),
                            pageable
                    ).map(candidateMapper::toDto);
                } else {
                    return candidateRepository.findAll(hrSp, pageable)
                            .map(candidateMapper::toDto);
                }

            case ADMIN:
                if (!criteriaList.isEmpty()) {
                    return candidateRepository.findAll(
                            new GenericSpecification<>(
                                    criteriaList,
                                    joinType,
                                    Candidate.class
                            ),
                            pageable
                    ).map(candidateMapper::toDto);
                } else {
                    return candidateRepository.findAll(pageable).map(candidateMapper::toDto);
                }

            default:
                return Page.empty(pageable);
        }
    }

    @Override
    public ResponseCandidateDTO getCandidateById(
            final Long userId,
            final Long candidateId
    ) {
        Candidate candidate = findCandidate(
                userId,
                candidateId
        );

        return candidateMapper.toDto(candidate);
    }

    @Override
    @Transactional
    public void deleteCandidate(
            final Long userId,
            final Long candidateId
    ) {
        Candidate candidate = findCandidate(
                userId,
                candidateId
        );

        candidateRepository.delete(candidate);
    }

    private Candidate findCandidate(
            final Long userId,
            final Long candidateId
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));

        if (user.getRole().equals(ADMIN)) {
            return candidateRepository.findById(candidateId)
                    .orElseThrow(() -> new DataNotFoundException("Кандидат не найден"));
        } else {
            CriteriaBuilder cb =
                    entityManager.getCriteriaBuilder();
            CriteriaQuery<Candidate> query =
                    cb.createQuery(Candidate.class);
            Root<Candidate> root =
                    query.from(Candidate.class);

            Predicate candidatePredicate =
                    cb.equal(root.get(Candidate_.id), candidateId);
            Predicate hrPredicate =
                    cb.equal(root.get(Candidate_.hr).get(User_.id), userId);
            Predicate finalPredicate =
                    cb.and(candidatePredicate, hrPredicate);

            query.select(root).where(finalPredicate);

            Candidate candidate;
            try {
                candidate =
                        entityManager.createQuery(query).getSingleResult();
            } catch (NoResultException e) {
                throw new DataNotFoundException("Кандидат не найден");
            }
            return candidate;
        }
    }
}

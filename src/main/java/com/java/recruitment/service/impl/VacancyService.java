package com.java.recruitment.service.impl;

import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.repositoty.VacancyRepository;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.IVacancyService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.filter.GenericSpecification;
import com.java.recruitment.service.filter.JoinType;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.service.model.user.User_;
import com.java.recruitment.service.model.vacancy.Vacancy;
import com.java.recruitment.service.model.vacancy.Vacancy_;
import com.java.recruitment.util.FilterParser;
import com.java.recruitment.util.NullPropertyCopyHelper;
import com.java.recruitment.web.dto.vacancy.RequestVacancyDTO;
import com.java.recruitment.web.dto.vacancy.ResponseVacancyDTO;
import com.java.recruitment.web.mapper.VacancyMapper;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.java.recruitment.service.model.user.Role.ADMIN;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VacancyService implements IVacancyService {

    private final VacancyMapper vacancyMapper;

    private final VacancyRepository vacancyRepository;

    private final UserRepository userRepository;

    private final EntityManager entityManager;

    @Override
    @Transactional
    public ResponseVacancyDTO postVacancy(
            final RequestVacancyDTO dto,
            final Long userId
    ) {

        LocalDateTime dateTime = LocalDateTime.now();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Рекрутер не найден не найдена"));

        Vacancy vacancy = vacancyMapper.toEntity(dto);
        vacancy.setCreatedDate(dateTime.toLocalDate());
        vacancy.setCreatedTime(dateTime.toLocalTime());
        vacancy.setRecruiter(user);
        vacancy.setActive(true);
        vacancyRepository.save(vacancy);
        return vacancyMapper.toDTO(vacancy);
    }

    @Override
    public ResponseVacancyDTO getVacancyById(final Long id) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Вакансия не найдена"));
        return vacancyMapper.toDTO(vacancy);
    }

    @SneakyThrows
    @Override
    public Page<ResponseVacancyDTO> getFilteredVacancy(
            final String criteriaJson,
            final JoinType joinType,
            final Pageable pageable
    ) {
        List<CriteriaModel> criteriaList = FilterParser.parseCriteriaJson(criteriaJson);

        Page<Vacancy> vacancies = criteriaList.isEmpty()
                ? vacancyRepository.findAll(pageable)
                : vacancyRepository.findAll(
                new GenericSpecification<>(
                        criteriaList,
                        joinType,
                        Vacancy.class
                ),
                pageable
        );

        return vacancies.map(vacancyMapper::toDTO);
    }

    @Override
    @Transactional
    public ResponseVacancyDTO updateVacancy(
            final Long userId,
            final RequestVacancyDTO dto
    ) {
        Vacancy vacancy = findVacancy(
                userId,
                dto.getId()
        );

        NullPropertyCopyHelper.copyNonNullProperties(dto, vacancy);
        Vacancy updatedVacancy = vacancyRepository.save(vacancy);
        return vacancyMapper.toDTO(updatedVacancy);
    }

    @Override
    @Transactional
    public void deleteVacancy(
            final Long userId,
            final Long vacancyId
    ) {
        Vacancy vacancy = findVacancy(
                userId,
                vacancyId
        );

        vacancyRepository.delete(vacancy);
    }

    private Vacancy findVacancy(
            final Long userId,
            final Long vacancyId
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));

        if (user.getRole().equals(ADMIN)) {
            return vacancyRepository.findById(vacancyId)
                    .orElseThrow(() -> new DataNotFoundException("Вакансия не найдена"));
        } else {
            CriteriaBuilder cb =
                    entityManager.getCriteriaBuilder();
            CriteriaQuery<Vacancy> query =
                    cb.createQuery(Vacancy.class);
            Root<Vacancy> root =
                    query.from(Vacancy.class);

            Predicate vacancyPredicate =
                    cb.equal(root.get(Vacancy_.id), vacancyId);
            Predicate recruiterPredicate =
                    cb.equal(root.get(Vacancy_.recruiter).get(User_.id), userId);
            Predicate finalPredicate =
                    cb.and(vacancyPredicate, recruiterPredicate);

            query.select(root).where(finalPredicate);

            Vacancy vacancy;
            try {
                vacancy =
                        entityManager.createQuery(query).getSingleResult();
            } catch (NoResultException e) {
                throw new DataNotFoundException("Вакансия не найдена");
            }
            return vacancy;
        }
    }
}
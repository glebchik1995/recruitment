package com.java.recruitment.service.impl;

import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.repositoty.VacancyRepository;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.IVacancyService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.filter.GenericSpecification;
import com.java.recruitment.service.filter.JoinType;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.service.model.vacancy.Vacancy;
import com.java.recruitment.util.AccessChecker;
import com.java.recruitment.util.FilterParser;
import com.java.recruitment.util.NullPropertyCopyHelper;
import com.java.recruitment.web.dto.vacancy.RequestVacancyDTO;
import com.java.recruitment.web.dto.vacancy.ResponseVacancyDTO;
import com.java.recruitment.web.mapper.VacancyMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VacancyService implements IVacancyService {

    private final VacancyMapper vacancyMapper;

    private final VacancyRepository vacancyRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public ResponseVacancyDTO postVacancy(
            final RequestVacancyDTO dto,
            final Long recruiterId
    ) {

        User user = userRepository.findById(recruiterId)
                .orElseThrow(() -> new DataNotFoundException("Рекрутер не найден не найдена"));

        Vacancy vacancy = vacancyMapper.toEntity(dto);
        vacancy.setCreatedDate(LocalDate.now());
        vacancy.setCreatedTime(LocalTime.now());
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
            final Long recruiterId,
            final RequestVacancyDTO dto
    ) {
        Vacancy vacancy = vacancyRepository.findById(dto.getId())
                .orElseThrow(() -> new DataNotFoundException("Вакансия не найден"));
        AccessChecker.checkAccess(
                vacancy.getRecruiter().getId(),
                recruiterId
        );
        NullPropertyCopyHelper.copyNonNullProperties(dto, vacancy);
        Vacancy updatedVacancy = vacancyRepository.save(vacancy);
        return vacancyMapper.toDTO(updatedVacancy);
    }

    @Override
    @Transactional
    public void deleteVacancy(
            final Long recruiterId,
            final Long id
    ) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Вакансия не найден"));

        AccessChecker.checkAccess(
                vacancy.getRecruiter().getId(),
                recruiterId
        );
        vacancyRepository.delete(vacancy);
    }
}
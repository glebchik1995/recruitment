package com.java.recruitment.service.impl;

import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.repositoty.VacancyRepository;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.IVacancyService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.filter.GenericSpecification;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.service.model.vacancy.Vacancy;
import com.java.recruitment.util.NullPropertyCopyHelper;
import com.java.recruitment.web.dto.vacancy.RequestVacancyDTO;
import com.java.recruitment.web.dto.vacancy.ResponseVacancyDTO;
import com.java.recruitment.web.mapper.VacancyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyService implements IVacancyService {

    private final VacancyMapper vacancyMapper;

    private final VacancyRepository vacancyRepository;

    private final UserRepository userRepository;

    @Override
    public ResponseVacancyDTO postVacancy(
            RequestVacancyDTO dto,
            Long recruiter_id
    ) {
        User user = userRepository.findById(recruiter_id)
                .orElseThrow(() -> new DataNotFoundException("Специалист не найден"));
        Vacancy vacancy = vacancyMapper.toEntity(dto);
        vacancy.setCreatedDate(LocalDate.now());
        vacancy.setCreatedTime(LocalTime.now());
        vacancy.setRecruiter(user);
        vacancyRepository.save(vacancy);
        return vacancyMapper.toDTO(vacancy);
    }

    @Override
    public ResponseVacancyDTO getById(final Long id) {
        return vacancyMapper.toDTO(vacancyRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Вакансия не найдена")));
    }

    @Override
    public Page<ResponseVacancyDTO> getAllVacancy(List<CriteriaModel> criteriaModelList, Pageable pageable) {
        Specification<Vacancy> specification
                = new GenericSpecification<>(criteriaModelList, Vacancy.class);
        Page<Vacancy> rooms = vacancyRepository.findAll(specification, pageable);
        return rooms.map(vacancyMapper::toDTO);
    }

    @Override
    public ResponseVacancyDTO updateVacancy(RequestVacancyDTO dto) {
        Vacancy vacancy = vacancyRepository.findById(dto.getId())
                .orElseThrow(() -> new DataNotFoundException("Вакансия не найден"));
        NullPropertyCopyHelper.copyNonNullProperties(dto, vacancy);
        Vacancy updatedVacancy = vacancyRepository.save(vacancy);
        return vacancyMapper.toDTO(updatedVacancy);
    }

    @Override
    public void deleteVacancy(Long id) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Вакансия не найден"));
        vacancyRepository.delete(vacancy);
    }

    @Override
    public boolean isVacancyOwner(
            final Long userId,
            final Long vacancy_id
    ) {
        return vacancyRepository.isVacancyOwner(userId, vacancy_id);
    }
}

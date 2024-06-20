package com.java.recruitment.service;

import com.java.recruitment.service.filter.JoinType;
import com.java.recruitment.web.dto.vacancy.RequestVacancyDTO;
import com.java.recruitment.web.dto.vacancy.ResponseVacancyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IVacancyService {


    ResponseVacancyDTO postVacancy(RequestVacancyDTO dto, Long userId);

    ResponseVacancyDTO getVacancyById(Long id);

    Page<ResponseVacancyDTO> getFilteredVacancy(
            String criteriaJson,
            JoinType joinType,
            Pageable pageable
    );

    ResponseVacancyDTO updateVacancy(Long userId, RequestVacancyDTO dto);

    void deleteVacancy(Long userId, Long id);
}

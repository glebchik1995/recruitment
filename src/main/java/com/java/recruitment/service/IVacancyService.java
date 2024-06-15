package com.java.recruitment.service;

import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.web.dto.vacancy.RequestVacancyDTO;
import com.java.recruitment.web.dto.vacancy.ResponseVacancyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IVacancyService {


    ResponseVacancyDTO postVacancy(RequestVacancyDTO dto, Long recruiter_id);

    ResponseVacancyDTO getVacancyById(Long id);

    Page<ResponseVacancyDTO> getAllVacancy(List<CriteriaModel> criteriaModelList, Pageable pageable);

    ResponseVacancyDTO updateVacancy(RequestVacancyDTO dto);

    void deleteVacancy(Long id);

    boolean isVacancyOwner(
            Long userId,
            Long vacancy_id
    );
}

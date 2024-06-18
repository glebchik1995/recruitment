package com.java.recruitment.web.controller.vacancy;

import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.repositoty.VacancyRepository;
import com.java.recruitment.service.IVacancyService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.util.FilterParser;
import com.java.recruitment.web.dto.vacancy.ResponseVacancyDTO;
import com.java.recruitment.web.mapper.VacancyMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "VACANCY Controller",
        description = "CRUD OPERATIONS WITH VACANCY"
)
@RestController
@RequestMapping("/vacancy")
@RequiredArgsConstructor
@LogInfo
public class VacancyController {

    private final IVacancyService vacancyService;

    private final VacancyRepository vacancyRepository;

    private final VacancyMapper mapper;

    @GetMapping
    @Operation(summary = "Получить всех вакансии")
    public Page<ResponseVacancyDTO> getAllVacancy(
            @RequestParam(required = false) final String criteriaJson,
            @ParameterObject final Pageable pageable
    ) throws BadRequestException {

        List<CriteriaModel> criteriaList;

        if (criteriaJson != null) {
            criteriaList = FilterParser.parseCriteriaJson(criteriaJson);
            return vacancyService.getFilteredVacancy(criteriaList, pageable);
        } else {
            return vacancyRepository.findAll(pageable).map(mapper::toDTO);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить вакансию по ID")
    public ResponseVacancyDTO getVacancyById(@PathVariable @Min(1) final Long id) {
        return vacancyService.getVacancyById(id);
    }
}

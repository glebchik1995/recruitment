package com.java.recruitment.web.controller.vacancy;

import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.service.IVacancyService;
import com.java.recruitment.service.filter.JoinType;
import com.java.recruitment.web.dto.vacancy.ResponseVacancyDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    @Operation(summary = "Получить всех вакансии")
    public Page<ResponseVacancyDTO> getAllVacancy(
            @RequestParam(required = false) final String criteriaJson,
            @RequestParam(required = false) final JoinType joinType,
            @ParameterObject Pageable pageable
    ) {
        return vacancyService.getFilteredVacancy(
                criteriaJson,
                joinType,
                pageable
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить вакансию по ID")
    public ResponseVacancyDTO getVacancyById(@PathVariable @Min(1) final Long id) {
        return vacancyService.getVacancyById(id);
    }
}

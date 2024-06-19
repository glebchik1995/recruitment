package com.java.recruitment.web.controller.vacancy;

import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.service.IVacancyService;
import com.java.recruitment.validation.line.ValidCriteriaJson;
import com.java.recruitment.web.dto.vacancy.ResponseVacancyDTO;
import com.java.recruitment.web.security.JwtEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal final JwtEntity currentUser,
            @RequestParam(required = false) @ValidCriteriaJson final String criteriaJson,
            @ParameterObject Pageable pageable
    ) {
        return vacancyService.getFilteredVacancy(
                currentUser.getId(),
                criteriaJson,
                pageable
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить вакансию по ID")
    public ResponseVacancyDTO getVacancyById(@PathVariable @Min(1) final Long id) {
        return vacancyService.getVacancyById(id);
    }
}

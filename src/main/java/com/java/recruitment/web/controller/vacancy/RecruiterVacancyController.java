package com.java.recruitment.web.controller.vacancy;

import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.service.IVacancyService;
import com.java.recruitment.validation.marker.OnCreate;
import com.java.recruitment.validation.marker.OnUpdate;
import com.java.recruitment.web.dto.vacancy.RequestVacancyDTO;
import com.java.recruitment.web.dto.vacancy.ResponseVacancyDTO;
import com.java.recruitment.web.security.expression.CustomSecurityExpression;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "RECRUITER VACANCY Controller",
        description = "CRUD OPERATIONS WITH VACANCY"
)
@RestController
@RequestMapping("/api/v1/recruiter/vacancy")
@RequiredArgsConstructor
@LogInfo
public class RecruiterVacancyController {

    private final IVacancyService vacancyService;

    private final CustomSecurityExpression expression;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Опубликовать вакансию")
    public ResponseVacancyDTO postVacancy(
            @Validated(OnCreate.class) @RequestBody final RequestVacancyDTO vacancyDTO
    ) {
        Long recruiter_id = expression.getIdFromContext();
        return vacancyService.postVacancy(vacancyDTO, recruiter_id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Редактировать вакансию")
    @PreAuthorize("@cse.isVacancyOwner(#vacancyDTO.id)")
    public ResponseVacancyDTO updateVacancy(
            @Validated(OnUpdate.class) @RequestBody final RequestVacancyDTO vacancyDTO
    ) {
        return vacancyService.updateVacancy(vacancyDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить вакансию")
    @PreAuthorize("@cse.isVacancyOwner(#id)")
    public void deleteVacancy(@PathVariable @Min(1) final Long id) {
        vacancyService.deleteVacancy(id);
    }
}

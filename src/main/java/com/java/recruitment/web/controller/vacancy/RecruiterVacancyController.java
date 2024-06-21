package com.java.recruitment.web.controller.vacancy;

import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.service.IVacancyService;
import com.java.recruitment.validator.marker.OnCreate;
import com.java.recruitment.validator.marker.OnUpdate;
import com.java.recruitment.web.dto.vacancy.RequestVacancyDTO;
import com.java.recruitment.web.dto.vacancy.ResponseVacancyDTO;
import com.java.recruitment.web.security.JwtEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "RECRUITER VACANCY Controller",
        description = "CRUD OPERATIONS WITH VACANCY"
)
@RestController
@RequestMapping("/recruiter/vacancy")
@RequiredArgsConstructor
@LogInfo
public class RecruiterVacancyController {

    private final IVacancyService vacancyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Опубликовать вакансию")
    public ResponseVacancyDTO postVacancy(
            @AuthenticationPrincipal final JwtEntity currentUser,
            @Validated(OnCreate.class) @RequestBody final RequestVacancyDTO dto
    ) {
        return vacancyService.postVacancy(dto, currentUser.getId());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Редактировать вакансию")
    public ResponseVacancyDTO updateVacancy(
            @AuthenticationPrincipal final JwtEntity currentUser,
            @Validated(OnUpdate.class) @RequestBody final RequestVacancyDTO vacancyDTO
    ) {
        return vacancyService.updateVacancy(currentUser.getId(), vacancyDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить вакансию")
    public void deleteVacancy(
            @AuthenticationPrincipal final JwtEntity currentUser,
            @PathVariable @Min(1) final Long id
    ) {
        vacancyService.deleteVacancy(currentUser.getId(), id);
    }
}

package com.java.recruitment.web.controller.vacancy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.repositoty.VacancyRepository;
import com.java.recruitment.service.IVacancyService;
import com.java.recruitment.service.filter.CriteriaModel;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Tag(
        name = "VACANCY Controller",
        description = "CRUD OPERATIONS WITH VACANCY"
)
@RestController
@RequestMapping("/api/v1/vacancy")
@RequiredArgsConstructor
@LogInfo
public class VacancyController {

    private final IVacancyService vacancyService;

    private final VacancyRepository vacancyRepository;

    private final VacancyMapper mapper;

    @GetMapping
    @Operation(summary = "Получить всех вакансии")
    public Page<ResponseVacancyDTO> getAllVacancy(
            @RequestParam(required = false) String criteriaJson,
            @ParameterObject Pageable pageable)
            throws BadRequestException {

        List<CriteriaModel> criteriaList;

        ObjectMapper objectMapper = new ObjectMapper();
        if (criteriaJson != null) {
            try {
                criteriaList = Arrays.asList(
                        objectMapper.readValue(
                                criteriaJson,
                                CriteriaModel[].class
                        )
                );

                return vacancyService.getAllVacancy(criteriaList, pageable);
            } catch (IOException ex) {
                throw new BadRequestException("Не удалось проанализировать условия", ex);
            }
        } else {
            return vacancyRepository.findAll(pageable).map(mapper::toDTO);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить вакансию по ID")
    public ResponseVacancyDTO getVacancyById(@PathVariable @Min(1) Long id) {
        return vacancyService.getVacancyById(id);
    }
}

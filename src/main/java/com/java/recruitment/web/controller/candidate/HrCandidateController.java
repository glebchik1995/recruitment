package com.java.recruitment.web.controller.candidate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.repositoty.CandidateRepository;
import com.java.recruitment.service.ICandidateService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.web.dto.candidate.CandidateDTO;
import com.java.recruitment.web.mapper.CandidateMapper;
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
        name = "CANDIDATE Controller",
        description = "CRUD OPERATIONS WITH CANDIDATE"
)
@RestController
@RequestMapping("/api/v1/hr/candidate")
@RequiredArgsConstructor
@LogInfo
public class HrCandidateController {

    private final ICandidateService candidateService;
    
    private final CandidateRepository candidateRepository;
    
    private final CandidateMapper mapper;

    @GetMapping
    @Operation(summary = "Получить всех кандидатов")
    public Page<CandidateDTO> getAllCandidates(
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

                return candidateService.getAllCandidates(criteriaList, pageable);
            } catch (IOException ex) {
                throw new BadRequestException("Не удалось проанализировать условия", ex);
            }
        } else {
            return candidateRepository.findAll(pageable).map(mapper::toDto);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить кндидата по ID")
    public CandidateDTO getCandidateById(@PathVariable @Min(1) Long id) {
        return candidateService.getCandidateById(id);
    }
}

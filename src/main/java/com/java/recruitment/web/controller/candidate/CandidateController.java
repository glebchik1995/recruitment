package com.java.recruitment.web.controller.candidate;

import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.service.ICandidateService;
import com.java.recruitment.service.filter.JoinType;
import com.java.recruitment.validator.json.ValidCriteriaJson;
import com.java.recruitment.validator.marker.OnCreate;
import com.java.recruitment.validator.marker.OnUpdate;
import com.java.recruitment.web.dto.candidate.RequestCandidateDTO;
import com.java.recruitment.web.dto.candidate.ResponseCandidateDTO;
import com.java.recruitment.web.security.JwtEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CANDIDATE Controller",
        description = "CRUD OPERATIONS WITH CANDIDATE"
)
@RestController
@RequestMapping("/candidate")
@RequiredArgsConstructor
@LogInfo
public class CandidateController {

    private final ICandidateService candidateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать кандидата")
    public ResponseCandidateDTO createCandidate(
            @AuthenticationPrincipal final JwtEntity currentUser,
            @Validated(OnCreate.class) @RequestBody final RequestCandidateDTO dto
    ) {
        return candidateService.createCandidate(
                currentUser.getId(),
                dto
        );
    }

    @GetMapping
    @Operation(summary = "Получить всех кандидатов")
    public Page<ResponseCandidateDTO> getAllCandidates(
            @AuthenticationPrincipal final JwtEntity currentUser,
            @RequestParam(required = false) @ValidCriteriaJson final String criteriaJson,
            @RequestParam(required = false) final JoinType joinType,
            @ParameterObject Pageable pageable
    ) {
        return candidateService.getFilteredCandidates(
                currentUser.getId(),
                criteriaJson,
                joinType,
                pageable
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить кандидата по ID")
    public ResponseCandidateDTO getCandidateById(
            @AuthenticationPrincipal final JwtEntity currentUser,
            @PathVariable @Min(1) final Long id
    ) {
        return candidateService.getCandidateById(
                currentUser.getId(),
                id
        );
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Изменить данные кандидата")
    public ResponseCandidateDTO editingDataCandidate(
            @AuthenticationPrincipal final JwtEntity currentUser,
            @Validated(OnUpdate.class) @RequestBody final RequestCandidateDTO dto
    ) {
        return candidateService.updateCandidate(
                currentUser.getId(),
                dto
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить кандидата")
    public void deleteCandidate(
            @AuthenticationPrincipal final JwtEntity currentUser,
            @PathVariable @Min(1) final Long id
    ) {
        candidateService.deleteCandidate(
                currentUser.getId(),
                id
        );
    }
}

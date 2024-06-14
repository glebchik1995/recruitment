package com.java.recruitment.web.controller.candidate;

import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.service.ICandidateService;
import com.java.recruitment.validation.marker.OnCreate;
import com.java.recruitment.validation.marker.OnUpdate;
import com.java.recruitment.web.dto.candidate.CandidateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CANDIDATE Controller",
        description = "CRUD OPERATIONS WITH CANDIDATE"
)
@RestController
@RequestMapping("/api/v1/candidate")
@RequiredArgsConstructor
@LogInfo
public class CandidateController {

    private final ICandidateService candidateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Изменить данные пользователя")
    public CandidateDTO createCandidate(
            @Validated(OnCreate.class) @RequestBody CandidateDTO candidate
    ) {
        return candidateService.createCandidate(candidate);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Изменить данные пользователя")
    public CandidateDTO editingDataCandidate(
            @Validated(OnUpdate.class) @RequestBody CandidateDTO candidate
    ) {
        return candidateService.updateCandidate(candidate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Изменить данные пользователя")
    public void deleteCandidate(@PathVariable @Min(1) final Long id) {
        candidateService.deleteCandidate(id);
    }

}

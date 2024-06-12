package com.java.recruitment.web.controller.hr;

import com.java.recruitment.aspect.log.ToLog;
import com.java.recruitment.service.impl.CandidateService;
import com.java.recruitment.web.dto.candidate.CandidateDTO;
import com.java.recruitment.validation.marker.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "HR Candidate Controller",
        description = "CRUD OPERATIONS WITH CANDIDATES"
)
@RestController
@RequestMapping("/api/v1/hr/candidate")
@RequiredArgsConstructor
@ToLog
public class HrCandidateController {

    private final CandidateService candidateService;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Изменить данные пользователя")
    public CandidateDTO editingDataCandidate(
            @Validated(OnUpdate.class) @RequestBody CandidateDTO candidate
    ) {
        return candidateService.updateCandidate(candidate);
    }
}

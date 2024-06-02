package com.java.recruitment.web.controller.hr;

import com.java.recruitment.service.impl.CandidateService;
import com.java.recruitment.web.dto.candidate.CandidateDTO;
import com.java.recruitment.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "HR - CANDIDATE", description = "CRUD OPERATIONS WITH CANDIDATES")
@RestController
@RequestMapping("/api/v1/hr/candidate")
@RequiredArgsConstructor
public class HrCandidateController {

    private final CandidateService candidateService;

    @PutMapping
    public ResponseEntity<CandidateDTO> editingDataCandidate(@Validated(OnUpdate.class) @RequestBody CandidateDTO candidate) {
        CandidateDTO updatedCandidate = candidateService.updateCandidate(candidate);
        return new ResponseEntity<>(updatedCandidate, HttpStatus.OK);
    }
}

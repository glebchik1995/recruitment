package com.java.recruitment.web.controller.hr;

import com.java.recruitment.web.dto.candidate.CandidateDTO;
import com.java.recruitment.web.dto.validation.OnCreate;
import com.java.recruitment.web.dto.validation.OnUpdate;
import com.java.recruitment.service.impl.CandidateService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "HR - CANDIDATE", description = "CRUD OPERATIONS WITH CANDIDATES")
@RestController
@RequestMapping("/hr/candidate")
@RequiredArgsConstructor
public class HrCandidateController {

    private final CandidateService candidateService;

    @PostMapping
    public ResponseEntity<CandidateDTO> createCandidate(@Validated(OnCreate.class) @RequestBody CandidateDTO candidate) {
        CandidateDTO createdCandidate = candidateService.createCandidate(candidate);
        return new ResponseEntity<>(createdCandidate, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<CandidateDTO>> getAllCandidates(@ParameterObject Pageable pageable) {
        Page<CandidateDTO> candidates = candidateService.getAllCandidates(pageable);
        return new ResponseEntity<>(candidates, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateDTO> getCandidateById(@PathVariable Long id) {
        CandidateDTO candidate = candidateService.getCandidateById(id);
        return new ResponseEntity<>(candidate, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateDTO> editingDataCandidate(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody CandidateDTO candidate) {
        CandidateDTO updatedCandidate = candidateService.updateCandidate(id, candidate);
        return new ResponseEntity<>(updatedCandidate, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

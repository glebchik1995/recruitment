package com.java.recruitment.controller.hr;

import com.java.recruitment.service.dto.candidate.CandidateDTO;
import com.java.recruitment.service.dto.validationGroup.OnCreate;
import com.java.recruitment.service.dto.validationGroup.OnUpdate;
import com.java.recruitment.service.impl.CandidateService;
import com.java.recruitment.service.model.candidate.Candidate;
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
    public ResponseEntity<Candidate> createCandidate(@Validated(OnCreate.class) @RequestBody CandidateDTO candidate) {
        Candidate createdCandidate = candidateService.createCandidate(candidate);
        return new ResponseEntity<>(createdCandidate, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Candidate>> getAllCandidates(@ParameterObject Pageable pageable) {
        Page<Candidate> candidates = candidateService.getAllCandidates(pageable);
        return new ResponseEntity<>(candidates, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable Long id) {
        Candidate candidate = candidateService.getCandidateById(id);
        return new ResponseEntity<>(candidate, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Candidate> editingDataCandidate(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody CandidateDTO candidate) {
        Candidate updatedCandidate = candidateService.updateCandidate(id, candidate);
        return new ResponseEntity<>(updatedCandidate, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

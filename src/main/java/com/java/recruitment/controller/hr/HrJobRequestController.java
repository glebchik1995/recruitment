package com.java.recruitment.controller.hr;

import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.service.dto.hiring.JobRequestDTO;
import com.java.recruitment.service.model.hiring.JobRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hr/job-request")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "HR - JOB-REQUEST", description = "CRUD OPERATIONS WITH JOB-REQUESTS")
public class HrJobRequestController {

    private final IJobRequestService jobRequestService;

    @PostMapping
    @Operation(summary = "Создание запроса на работу")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createJobRequest(@Valid @RequestBody JobRequestDTO jobRequestDto) {
        JobRequest jobRequest = jobRequestService.createJobRequest(jobRequestDto);
        return new ResponseEntity<>(jobRequest, HttpStatus.CREATED);
    }
}

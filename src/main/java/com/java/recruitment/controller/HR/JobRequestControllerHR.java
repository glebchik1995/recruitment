package com.java.recruitment.controller.HR;

import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.service.dto.hiring.JobRequestDto;
import com.java.recruitment.service.model.hiring.JobRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hr/job")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Job request - HR", description = "Взаимодействие с запросами на работу")
public class JobRequestControllerHR {

    private final IJobRequestService jobRequestService;

    @PostMapping
    @Operation(summary = "Создание запроса на работу")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createJobRequest(@RequestBody JobRequestDto jobRequestDto) {
        JobRequest jobRequest = jobRequestService.createJobRequest(jobRequestDto);
        return new ResponseEntity<>(jobRequest, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<JobRequest>> getAllJobRequests(@ParameterObject Pageable pageable) {
        Page<JobRequest> jobRequests = jobRequestService.getAllJobRequests(pageable);
        return new ResponseEntity<>(jobRequests, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobRequest> getJobRequestById(@PathVariable Long id) {
        JobRequest jobRequest = jobRequestService.getById(id);
        return new ResponseEntity<>(jobRequest, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobRequest> updateJobRequest(@PathVariable Long id, @RequestBody JobRequestDto jobRequestDto) {
        JobRequest updatedJobRequest = jobRequestService.updateJobRequest(id, jobRequestDto);
        return new ResponseEntity<>(updatedJobRequest, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobRequest(@PathVariable Long id) {
        jobRequestService.deleteJobRequest(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

package com.java.recruitment.controller.interviewer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.recruitment.repositoty.JobRequestRepository;
import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.service.dto.hiring.ChangeJobRequestStatusDTO;
import com.java.recruitment.service.dto.hiring.JobRequestDTO;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.mapper.jobRequest.JobRequestMapper;
import com.java.recruitment.service.model.hiring.JobRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/hr/job")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "HR - INTERVIEWER-REQUEST", description = "CRUD OPERATIONS WITH JOB-REQUESTS")
public class InterviewerJobRequestController {

    private final IJobRequestService jobRequestService;

    private final JobRequestRepository jobRequestRepository;

    private final JobRequestMapper mapper;


    @GetMapping
    public ResponseEntity<Page<JobRequestDTO>> getAllJobRequests(@RequestParam(required = false) String criteriaJson, @ParameterObject Pageable pageable) throws BadRequestException {
        CriteriaModel model = null;
        if (criteriaJson != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                model = objectMapper.readValue(criteriaJson, CriteriaModel.class);
            } catch (IOException ex) {
                throw new BadRequestException("Не удалось проанализировать условия", ex);
            }
        }
        if (model != null) {

            Page<JobRequestDTO> jobRequests = jobRequestService.getAllJobRequests(model, pageable);
            return new ResponseEntity<>(jobRequests, HttpStatus.OK);
        } else {
            Page<JobRequestDTO> jobRequests = jobRequestRepository.findAll(pageable).map(mapper::toDto);
            return new ResponseEntity<>(jobRequests, HttpStatus.OK);

        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<JobRequest> getJobRequestById(@PathVariable Long id) {
        JobRequest jobRequest = jobRequestService.getById(id);
        return new ResponseEntity<>(jobRequest, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobRequest> updateStatusJobRequest(@PathVariable Long id,
                                                             @RequestBody ChangeJobRequestStatusDTO jobRequestDto) {
        JobRequest updatedJobRequest = jobRequestService.updateJobRequest(id, jobRequestDto);
        return new ResponseEntity<>(updatedJobRequest, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobRequest(@PathVariable Long id) {
        jobRequestService.deleteJobRequest(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
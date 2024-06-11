package com.java.recruitment.web.controller.interviewer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.recruitment.repositoty.JobRequestRepository;
import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.web.dto.hiring.ChangeJobRequestStatusDTO;
import com.java.recruitment.web.dto.hiring.JobResponseDTO;
import com.java.recruitment.web.mapper.JobRequestMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/interviewer/job-request")
@RequiredArgsConstructor
@Tag(
        name = "INTERVIEWER - JOB_REQUESTS",
        description = "CRUD OPERATIONS WITH JOB-REQUESTS"
)
public class InterviewerJobRequestController {

    private final IJobRequestService jobRequestService;

    private final JobRequestRepository jobRequestRepository;

    private final JobRequestMapper mapper;

    @GetMapping
    public Page<JobResponseDTO> getAllJobRequests(
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

                return jobRequestService.getAllJobRequests(criteriaList, pageable);
            } catch (IOException ex) {
                throw new BadRequestException("Не удалось проанализировать условия", ex);
            }
        } else {
            return jobRequestRepository.findAll(pageable).map(mapper::toDto);
        }
    }

    @GetMapping("/{id}")
    public JobResponseDTO getJobRequestById(@PathVariable Long id) {
        return jobRequestService.getJobRequestById(id);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("@cse.canAccessJobRequest(#jobRequestDto.id)")
    public JobResponseDTO updateStatusJobRequest(@RequestBody ChangeJobRequestStatusDTO jobRequestDto) {
        return jobRequestService.updateJobRequest(jobRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteJobRequest(@PathVariable Long id) {
        jobRequestService.deleteJobRequest(id);
    }
}
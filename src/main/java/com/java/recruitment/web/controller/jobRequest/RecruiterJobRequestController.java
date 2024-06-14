package com.java.recruitment.web.controller.jobRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.repositoty.JobRequestRepository;
import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.web.dto.hiring.ChangeJobRequestStatusDTO;
import com.java.recruitment.web.dto.hiring.JobResponseDTO;
import com.java.recruitment.web.mapper.JobRequestMapper;
import com.java.recruitment.web.security.expression.CustomSecurityExpression;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Tag(
        name = "Interviewer Job Request Controller",
        description = "CRUD OPERATIONS WITH JOB-REQUESTS"
)
@RestController
@RequestMapping("/api/v1/recruiter/job-request")
@LogInfo
@Validated
@RequiredArgsConstructor
public class RecruiterJobRequestController {

    private final IJobRequestService jobRequestService;

    private final JobRequestRepository jobRequestRepository;

    private final CustomSecurityExpression expression;

    private final JobRequestMapper mapper;

    @GetMapping
    @Operation(summary = "Получить все заявки")
    public Page<JobResponseDTO> getAllJobRequests(
            @RequestParam(required = false) String criteriaJson,
            @ParameterObject Pageable pageable)
            throws BadRequestException {

        Long recruiter_id = expression.getIdFromContext();

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

                return jobRequestService.getAllJobRequests(criteriaList, recruiter_id, pageable);
            } catch (IOException ex) {
                throw new BadRequestException("Не удалось проанализировать условия", ex);
            }
        } else {
            return jobRequestRepository.findJobRequestsForRecruiter(recruiter_id, pageable).map(mapper::toDto);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить заявку по ID")
    @PreAuthorize("@cse.canAccessJobRequestForRecruiter(#id)")
    public JobResponseDTO getJobRequestById(@PathVariable @Min(1) Long id) {
        return jobRequestService.getJobRequestById(id);
    }

    @PutMapping
    @Operation(summary = "Изменить статус заявки")
    @PreAuthorize("@cse.canAccessJobRequestForRecruiter(#jobRequestDto.id)")
    public JobResponseDTO updateStatusJobRequest(@RequestBody @Valid ChangeJobRequestStatusDTO jobRequestDto) {
        return jobRequestService.updateJobRequest(jobRequestDto);
    }
}
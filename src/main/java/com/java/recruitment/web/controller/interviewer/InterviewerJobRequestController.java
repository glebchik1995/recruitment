package com.java.recruitment.web.controller.interviewer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.recruitment.aspect.log.ToLogInfo;
import com.java.recruitment.repositoty.JobRequestRepository;
import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.web.dto.hiring.ChangeJobRequestStatusDTO;
import com.java.recruitment.web.dto.hiring.JobResponseDTO;
import com.java.recruitment.web.mapper.JobRequestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/api/v1/interviewer/job-request")
@ToLogInfo
@Validated
@RequiredArgsConstructor
public class InterviewerJobRequestController {

    private final IJobRequestService jobRequestService;

    private final JobRequestRepository jobRequestRepository;

    private final JobRequestMapper mapper;

    @GetMapping
    @Operation(summary = "Получить все заявки")
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
    @Operation(summary = "Получить заявку по ID")
    public JobResponseDTO getJobRequestById(@PathVariable @Min(1) Long id) {
        return jobRequestService.getJobRequestById(id);
    }

    @PutMapping
    @Operation(summary = "Изменить статус заявки")
    public JobResponseDTO updateStatusJobRequest(@RequestBody @Valid ChangeJobRequestStatusDTO jobRequestDto) {
        return jobRequestService.updateJobRequest(jobRequestDto);
    }
}
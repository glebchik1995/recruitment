package com.java.recruitment.web.controller.jobRequest;

import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.web.dto.jobRequest.JobRequestDTO;
import com.java.recruitment.web.dto.jobRequest.JobResponseDTO;
import com.java.recruitment.web.security.expression.CustomSecurityExpression;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "HR Job Request Controller",
        description = "CRUD OPERATIONS WITH JOB-REQUESTS"
)
@RestController
@RequestMapping(value = "/hr/job-request")
@RequiredArgsConstructor
@LogInfo
public class HrJobRequestController {

    private final IJobRequestService jobRequestService;

    private final CustomSecurityExpression expression;

    @PostMapping
    @Operation(summary = "Создание заявки на работу")
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponseDTO createJobRequest(
            @Valid @ModelAttribute final JobRequestDTO jobRequestDto
    ) {
        Long hrId = expression.getIdFromContext();
        return jobRequestService.createJobRequest(jobRequestDto, hrId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить заявку по ID")
    @PreAuthorize("@cse.isJobRequestOwner(#id)")
    public JobResponseDTO getJobRequestById(@PathVariable @Min(1) final Long id) {
        return jobRequestService.getJobRequestById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить заявку на работу по ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@cse.isJobRequestOwner(#id)")
    public void deleteJobRequest(@PathVariable @Min(1) final Long id) {
        jobRequestService.deleteJobRequest(id);
    }
}

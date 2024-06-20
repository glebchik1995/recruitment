package com.java.recruitment.web.controller.jobRequest;

import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.web.dto.jobRequest.JobRequestDTO;
import com.java.recruitment.web.dto.jobRequest.JobResponseDTO;
import com.java.recruitment.web.security.JwtEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping
    @Operation(summary = "Создание заявки на работу")
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponseDTO createJobRequest(
            @AuthenticationPrincipal final JwtEntity currentUser,
            @Valid @ModelAttribute final JobRequestDTO jobRequestDto
    ) {
        return jobRequestService.createJobRequest(
                currentUser.getId(),
                jobRequestDto
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить заявку на работу по ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJobRequest(
            @AuthenticationPrincipal final JwtEntity currentUser,
            @PathVariable @Min(1) final Long id
    ) {
        jobRequestService.deleteJobRequest(
                currentUser.getId(),
                id
        );
    }
}

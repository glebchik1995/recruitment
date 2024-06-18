package com.java.recruitment.web.controller.jobRequest;

import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.web.dto.jobRequest.ChangeJobRequestStatusDTO;
import com.java.recruitment.web.dto.jobRequest.JobResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Interviewer Job Request Controller",
        description = "CRUD OPERATIONS WITH JOB-REQUESTS"
)
@RestController
@RequestMapping("/recruiter/job-request")
@LogInfo
@RequiredArgsConstructor
public class RecruiterJobRequestController {

    private final IJobRequestService jobRequestService;

    @PutMapping
    @Operation(summary = "Изменить статус заявки")
    public JobResponseDTO updateStatusJobRequest(
            @RequestBody @Valid ChangeJobRequestStatusDTO jobRequestDto
    ) {
        return jobRequestService.updateJobRequest(jobRequestDto);
    }
}
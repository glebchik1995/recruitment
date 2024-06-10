package com.java.recruitment.web.controller.hr;

import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.web.dto.hiring.JobRequestDTO;
import com.java.recruitment.web.dto.hiring.JobResponseDTO;
import com.java.recruitment.web.dto.validation.OnCreate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/hr/job-request")
@RequiredArgsConstructor
@Tag(name = "HR - JOB-REQUEST", description = "CRUD OPERATIONS WITH JOB-REQUESTS")
public class HrJobRequestController {

    private final IJobRequestService jobRequestService;

    @PostMapping
    @Operation(summary = "Создание запроса на работу")
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponseDTO createJobRequest(
            @Validated(OnCreate.class) @ModelAttribute JobRequestDTO jobRequestDto
    ) {
        return jobRequestService.createJobRequest(jobRequestDto);
    }
}

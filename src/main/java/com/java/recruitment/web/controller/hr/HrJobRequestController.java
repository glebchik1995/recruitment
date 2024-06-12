package com.java.recruitment.web.controller.hr;

import com.java.recruitment.aspect.log.ToLog;
import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.web.dto.hiring.JobRequestDTO;
import com.java.recruitment.web.dto.hiring.JobResponseDTO;
import com.java.recruitment.validation.marker.OnCreate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "HR Job Request Controller",
        description = "CRUD OPERATIONS WITH JOB-REQUESTS"
)
@RestController
@RequestMapping(value = "/hr/job-request")
@RequiredArgsConstructor
@ToLog
public class HrJobRequestController {

    private final IJobRequestService jobRequestService;

    @PostMapping
    @Operation(summary = "Создание заявки на работу")
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponseDTO createJobRequest(
            @Validated(OnCreate.class) @ModelAttribute JobRequestDTO jobRequestDto
    ) {
        return jobRequestService.createJobRequest(jobRequestDto);
    }
}

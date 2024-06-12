package com.java.recruitment.web.controller.admin;

import com.java.recruitment.aspect.log.ToLog;
import com.java.recruitment.service.IJobRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Admin Job Request Controller",
        description = "CRUD OPERATIONS WITH JOB-REQUESTS"
)
@RestController
@RequestMapping("/api/v1/admin/job-request")
@RequiredArgsConstructor
@Validated
@ToLog
public class AdminJobRequestController {

    private final IJobRequestService jobRequestService;

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить заявку на работу по ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJobRequest(@PathVariable @Min(1) Long id) {
        jobRequestService.deleteJobRequest(id);
    }
}

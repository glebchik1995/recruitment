package com.java.recruitment.web.controller.jobRequest;

import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.service.filter.JoinType;
import com.java.recruitment.validation.line.ValidCriteriaJson;
import com.java.recruitment.web.dto.jobRequest.JobResponseDTO;
import com.java.recruitment.web.security.JwtEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.simpleframework.xml.core.Validate;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Job Request Controller",
        description = "CRUD OPERATIONS WITH JOB-REQUESTS"
)
@RestController
@RequestMapping(value = "/job-request")
@RequiredArgsConstructor
@Validate
@LogInfo
public class JobRequestController {

    private final IJobRequestService jobRequestService;

    @GetMapping
    public Page<JobResponseDTO> getAllJobRequest(
            @AuthenticationPrincipal final JwtEntity currentUser,
            @RequestParam(required = false) @ValidCriteriaJson final String criteriaJson,
            @RequestParam(required = false) final JoinType joinType,
            @ParameterObject Pageable pageable
    ) {
        return jobRequestService.getFilteredJobRequests(
                currentUser.getId(),
                criteriaJson,
                joinType,
                pageable
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить заявку по ID")
    public JobResponseDTO getJobRequestById(
            @AuthenticationPrincipal final JwtEntity currentUser,
            @PathVariable @Min(1) final Long id
    ) {
        return jobRequestService.getJobRequestById(
                currentUser.getId(),
                id
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить ссылку на скачивание файлов по ID заявки на работу")
    public ResponseEntity<String> downloadFiles(
            @AuthenticationPrincipal final JwtEntity currentUser,
            @PathVariable @Min(1) final Long id) {

        String downloadLinks = jobRequestService.downloadByJobRequestId(
                currentUser.getId(),
                id
        );
        return ResponseEntity.ok(downloadLinks);
    }
}

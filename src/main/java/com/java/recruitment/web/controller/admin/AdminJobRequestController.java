package com.java.recruitment.web.controller.admin;

import com.java.recruitment.service.IJobRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/job-request")
@RequiredArgsConstructor
@Tag(
        name = "ADMIN- JOB_REQUESTS",
        description = "CRUD OPERATIONS WITH JOB-REQUESTS"
)
public class AdminJobRequestController {

    private final IJobRequestService jobRequestService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobRequest(@PathVariable Long id) {
        jobRequestService.deleteJobRequest(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

package com.java.recruitment.service.dto.hiring;

import com.java.recruitment.service.model.attachment.AttachedFile;
import com.java.recruitment.service.model.candidate.Candidate;
import com.java.recruitment.service.model.hiring.JobRequestStatus;
import com.java.recruitment.service.model.hr.HR;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequestDTO {

    private Long id;

    @NotNull
    @Schema(description = "job_request_status", example = "NEW")
    private JobRequestStatus status;

    @NotNull
    private HR hr;

    @NotNull
    private Candidate candidate;

    private List<AttachedFile> files;

    private String description;
}

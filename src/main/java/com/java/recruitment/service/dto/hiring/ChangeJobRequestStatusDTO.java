package com.java.recruitment.service.dto.hiring;

import com.java.recruitment.service.model.hiring.JobRequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeJobRequestStatusDTO {

    private Long id;

    @NotNull
    @Schema(description = "job_request_status", example = "NEW")
    private JobRequestStatus status;
}

package com.java.recruitment.web.dto.hiring;

import com.java.recruitment.service.model.hiring.Status;
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
    private Status status;
}

package com.java.recruitment.web.dto.hiring;

import com.java.recruitment.service.model.hiring.Status;
import com.java.recruitment.validation.EnumValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeJobRequestStatusDTO {

    @Min(1)
    private Long id;

    @NotNull
    @EnumValid(enumClass = Status.class)
    @Schema(description = "job_request_status", example = "NEW")
    private Status status;
}


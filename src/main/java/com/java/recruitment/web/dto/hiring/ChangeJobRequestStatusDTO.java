package com.java.recruitment.web.dto.hiring;

import com.java.recruitment.service.model.jobRequest.Status;
import com.java.recruitment.validation.enums.EnumAllowedConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChangeJobRequestStatusDTO {

    @Min(1)
    private Long id;

    @NotNull
    @Schema(description = "Статус заявки на найм", example = "INTERVIEW")
    @EnumAllowedConstraint(
            enumClass = Status.class,
            allowed =
                    {
                            "CANDIDATE_FOUND",
                            "INTERVIEW",
                            "HIRED",
                            "REJECTED"
                    }
    )
    private Status status;
}


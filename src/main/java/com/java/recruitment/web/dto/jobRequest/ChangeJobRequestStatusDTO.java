package com.java.recruitment.web.dto.jobRequest;

import com.java.recruitment.service.model.jobRequest.Status;
import com.java.recruitment.validator.enums.EnumAllowedConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class ChangeJobRequestStatusDTO {

    @Min(1)
    private Long id;

    @NotNull
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


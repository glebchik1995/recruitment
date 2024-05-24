package com.java.recruitment.web.dto.hiring;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.java.recruitment.service.model.hiring.Status;
import com.java.recruitment.service.model.hr.HR;
import com.java.recruitment.web.dto.candidate.CandidateDTO;
import com.java.recruitment.web.dto.validation.OnCreate;
import com.java.recruitment.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequestDTO {

    private Long id;

    @NotNull
    private Status status;

    @NotNull
    private HR hr;

    @NotNull
    private CandidateDTO candidate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> files;

    @Length(max = 255, groups = {OnCreate.class, OnUpdate.class})
    private String description;
}

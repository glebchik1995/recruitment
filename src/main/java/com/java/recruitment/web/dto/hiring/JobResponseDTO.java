package com.java.recruitment.web.dto.hiring;

import com.java.recruitment.service.model.hiring.Status;
import com.java.recruitment.web.dto.candidate.CandidateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@Schema(description = "Job Response DTO")
public class JobResponseDTO {

    private Long id;

    private Status status;

    private Long hrId;

    private CandidateDTO candidate;

    private List<String> files;

    private String description;
}

package com.java.recruitment.web.dto.hiring;

import com.java.recruitment.service.model.hiring.Status;
import com.java.recruitment.web.dto.candidate.CandidateDTO;
import com.java.recruitment.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

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
    private Long hrId;

    @NotNull
    private CandidateDTO candidate;

    @Nullable
    private MultipartFile file;

    @Length(max = 255)
    @Nullable
    private String description;
}

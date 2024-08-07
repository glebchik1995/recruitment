package com.java.recruitment.web.dto.jobRequest;

import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class JobRequestDTO {

    private Long id;

    @Min(1)
    private Long candidateId;

    @Min(1)
    private Long vacancyId;

    @Nullable
    private MultipartFile[] files;

    @Nullable
    private String description;
}

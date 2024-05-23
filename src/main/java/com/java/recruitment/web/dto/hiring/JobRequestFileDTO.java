package com.java.recruitment.web.dto.hiring;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "File DTO")
public class JobRequestFileDTO {

    private Long jobRequestId;

    @NotNull
    private MultipartFile file;
}

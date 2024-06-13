package com.java.recruitment.web.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
@Schema(description = "Delete File DTO")
public class DeleteFileDTO {

    @Min(1)
    private Long jobRequestId;

    @NotBlank
    private String fileName;

}

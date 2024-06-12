package com.java.recruitment.web.dto.file;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteFileDTO {

    @Min(1)
    @NotNull
    private Long jobRequestId;

    @NotBlank
    private String fileName;

}

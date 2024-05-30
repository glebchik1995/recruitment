package com.java.recruitment.web.dto.file;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteFileDTO {

    @Positive
    @NotNull
    private Long jobRequestId;

    @NotNull
    private String fileName;

}

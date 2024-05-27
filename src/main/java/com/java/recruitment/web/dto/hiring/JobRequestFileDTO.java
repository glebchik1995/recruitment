package com.java.recruitment.web.dto.hiring;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class JobRequestFileDTO {

    @NotNull
    private MultipartFile file;
}

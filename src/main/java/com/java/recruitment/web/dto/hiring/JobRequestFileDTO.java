package com.java.recruitment.web.dto.hiring;

import com.java.recruitment.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class JobRequestFileDTO {

    @NotNull(groups = OnUpdate.class)
    private MultipartFile file;
}

package com.java.recruitment.web.dto.hiring;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Schema(description = "File DTO")
public class JobRequestFileInfoDTO {

    private Long id;

    private String name;

    private Long size;

    private LocalDate uploadDate;
}

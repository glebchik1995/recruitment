package com.java.recruitment.service.model.hiring;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class JobRequestFile {

    private MultipartFile file;
}

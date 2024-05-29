package com.java.recruitment.web.controller.interviewer;

import com.java.recruitment.service.IFileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Attached File - HR or Interviewer", description = "Взаимодействие с вложенными файлами к заявке на работу")
@RestController
@RequestMapping("/api/v1/interviewer/files")
@RequiredArgsConstructor
public class InterviewerFileController {

    private final IFileService fileService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Resource> downloadFiles(@PathVariable Long id) {
        String downloadUrl = fileService.download(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, downloadUrl);
        return ResponseEntity.status(HttpStatus.FOUND)
                .headers(headers)
                .build();
    }
}

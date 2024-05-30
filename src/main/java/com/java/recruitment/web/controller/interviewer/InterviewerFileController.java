package com.java.recruitment.web.controller.interviewer;

import com.java.recruitment.service.IFileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    @GetMapping(value = "{id}")
    public ResponseEntity<String> downloadFiles(@PathVariable Long id) {
        String downloadLinks = fileService.download(id);
        return ResponseEntity.ok(downloadLinks);
    }
}

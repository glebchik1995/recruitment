package com.java.recruitment.web.controller.hr;

import com.java.recruitment.service.IFileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "HR - FILE", description = "CRUD OPERATIONS WITH ATTACHED FILES")
@RestController
@RequestMapping("/hr/files")
@RequiredArgsConstructor
public class HrFileController {

    private final IFileService fileService;

    @PostMapping
    public ResponseEntity<String> upload(@Validated @ModelAttribute MultipartFile attachment) {
        return new ResponseEntity<>(fileService.upload(attachment), HttpStatus.CREATED);
    }
}

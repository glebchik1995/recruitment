package com.java.recruitment.web.controller.hr;

import com.java.recruitment.service.IFileService;
import com.java.recruitment.web.dto.hiring.JobRequestFileDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "HR - FILE", description = "CRUD OPERATIONS WITH ATTACHED FILES")
@RestController
@RequestMapping("/hr/files")
@RequiredArgsConstructor
public class HrFileController {

    private final IFileService fileService;

    @PostMapping
    public ResponseEntity<JobRequestFileDTO> upload(@RequestParam MultipartFile attachment) {
        return new ResponseEntity<>(fileService.upload(attachment), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws IOException {
        fileService.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

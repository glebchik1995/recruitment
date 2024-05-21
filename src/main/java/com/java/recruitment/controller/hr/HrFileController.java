package com.java.recruitment.controller.hr;

import com.java.recruitment.service.IFileService;
import com.java.recruitment.service.model.attachment.AttachedFile;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "HR - FILE", description = "CRUD OPERATIONS WITH ATTACHED FILES")
@RestController
@RequestMapping("/hr/files")
@RequiredArgsConstructor
public class HrFileController {

    private final IFileService fileService;

    @PostMapping
    public ResponseEntity<AttachedFile> loadFile(@RequestParam("file") MultipartFile file) {
        AttachedFile uploadedFile = fileService.loadFile(file);
        return new ResponseEntity<>(uploadedFile, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

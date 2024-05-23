package com.java.recruitment.web.controller.hr;

import com.java.recruitment.service.IFileService;
import com.java.recruitment.service.model.attachment.AttachedFile;
import com.java.recruitment.web.dto.hiring.JobRequestFileDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "HR - FILE", description = "CRUD OPERATIONS WITH ATTACHED FILES")
@RestController
@RequestMapping("/hr/files")
@RequiredArgsConstructor
public class HrFileController {

    private final IFileService fileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AttachedFile> loadFile(@Valid @RequestBody JobRequestFileDTO file) {
        AttachedFile uploadedFile = fileService.loadFile(file);
        return new ResponseEntity<>(uploadedFile, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

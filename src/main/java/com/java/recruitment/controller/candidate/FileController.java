package com.java.recruitment.controller.candidate;

import com.java.recruitment.service.IFileService;
import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.service.model.attachment.AttachedFile;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Attached File - Candidate", description = "Взаимодействие с вложенными файлами к заявке на работу")
@RestController
@RequestMapping("/candidate/files")
@RequiredArgsConstructor
public class FileController {

    private final IFileService fileService;

    @PostMapping
    public ResponseEntity<AttachedFile> uploadFile(@RequestParam("file") MultipartFile file) {
        AttachedFile uploadedFile = fileService.uploadFile(file);
        return new ResponseEntity<>(uploadedFile, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFilesByJobRequestId(@PathVariable Long id) throws IOException {
        List<AttachedFile> files = fileService.downloadFile(id);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);

        for (AttachedFile file : files) {
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zos.putNextEntry(zipEntry);
            zos.write(file.getData());
            zos.closeEntry();
        }

        zos.finish();
        zos.close();

        ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"files.zip\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

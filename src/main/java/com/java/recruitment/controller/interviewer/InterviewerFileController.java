package com.java.recruitment.controller.interviewer;

import com.java.recruitment.service.IFileService;
import com.java.recruitment.service.model.attachment.AttachedFile;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Attached File - HR or Interviewer", description = "Взаимодействие с вложенными файлами к заявке на работу")
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class InterviewerFileController {

    private final IFileService fileService;

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
}

package com.java.recruitment.web.controller.admin;

import com.java.recruitment.service.IFileService;
import com.java.recruitment.web.dto.file.DeleteFileDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ADMIN - FILE", description = "CRUD OPERATIONS WITH FILES")
@RestController
@RequestMapping("/api/v1/admin/file")
@RequiredArgsConstructor
public class AdminFileController {

    private final IFileService fileService;

    @DeleteMapping
    public ResponseEntity<String> deleteFile(@Validated @RequestBody final DeleteFileDTO dto) {
        fileService.delete(dto);
        return ResponseEntity.ok("Файл " + dto.getFileName() + " успешно удалено");
    }
}

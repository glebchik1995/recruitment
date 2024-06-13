package com.java.recruitment.web.controller.admin;

import com.java.recruitment.aspect.log.ToLogInfo;
import com.java.recruitment.service.IFileService;
import com.java.recruitment.web.dto.file.DeleteFileDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Admin File Controller",
        description = "CRUD OPERATIONS WITH FILES"
)
@ToLogInfo
@RestController
@RequestMapping("/api/v1/admin/file")
@RequiredArgsConstructor
public class AdminFileController {

    private final IFileService fileService;

    @DeleteMapping
    @Operation(summary = "Удалить вложенный файл в заявку на работу")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteFile(@Valid @RequestBody final DeleteFileDTO dto) {
        fileService.delete(dto);
        return ResponseEntity.ok("Файл " + dto.getFileName() + " успешно удален");
    }
}

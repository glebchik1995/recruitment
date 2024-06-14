package com.java.recruitment.web.controller.file;

import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.service.IFileService;
import com.java.recruitment.web.dto.file.DeleteFileDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "HR File Controller",
        description = "CRUD OPERATIONS WITH FILES"
)
@LogInfo
@RestController
@RequestMapping("/api/v1/hr/file")
@RequiredArgsConstructor
public class HrFileController {

    private final IFileService fileService;

    @DeleteMapping
    @Operation(summary = "Удалить вложенный файл в заявку на работу")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @PreAuthorize("@cse.canAccessJobRequest(#dto.jobRequestId)")
    public void deleteFile(@Valid @RequestBody final DeleteFileDTO dto) {
        fileService.delete(dto);
    }
}
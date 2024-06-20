package com.java.recruitment.web.controller.file;

import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.service.IFileService;
import com.java.recruitment.web.security.JwtEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Interviewer File Controller",
        description = "CRUD OPERATIONS WITH FILES"
)
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Validated
@LogInfo
public class RecruiterFileController {

    private final IFileService fileService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить ссылку на скачивание файлов по ID заявки на работу")
    public ResponseEntity<String> downloadFiles(
            @AuthenticationPrincipal final JwtEntity currentUser,
            @PathVariable @Min(1) final Long id) {

        String downloadLinks = fileService.download(
                currentUser.getId(),
                id
        );
        return ResponseEntity.ok(downloadLinks);
    }
}

package com.java.recruitment.web.controller.interviewer;

import com.java.recruitment.service.IFileService;
import com.java.recruitment.web.dto.hiring.JobRequestFileInfoDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Attached File - HR or Interviewer", description = "Взаимодействие с вложенными файлами к заявке на работу")
@RestController
@RequestMapping("/interviewer/files")
@RequiredArgsConstructor
public class InterviewerFileController {

    private final IFileService fileService;

//    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    public ResponseEntity<Resource> download(@PathVariable("id") Long id) {
//        try {
//            JobRequestFileInfoDTO foundFile = fileService.findById(id);
//            Resource resource = fileService.download(foundFile.getKey());
//            return ResponseEntity.ok()
//                    .header("Content-Disposition", "attachment; filename=" + foundFile.getName())
//                    .body(resource);
//        } catch (IOException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
}

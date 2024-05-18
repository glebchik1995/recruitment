package com.java.recruitment.controller.HR;

import com.java.recruitment.service.IHiringService;
import com.java.recruitment.service.dto.hiring.HiringRequestDto;
import com.java.recruitment.service.model.hiring.HiringRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hr/hiring")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Hiring - HR", description = "Возможность совершать различные действия с услугами в роле пользователя")
public class HiringHRController {

    private final IHiringService hiringService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitHiringRequest(@RequestBody HiringRequestDto hiringRequestDto) {
        HiringRequest hiringRequest = hiringService.submitHiringRequest(hiringRequestDto);
        return new ResponseEntity<>(hiringRequest, HttpStatus.CREATED);
    }

    @GetMapping("/requests")
    public ResponseEntity<?> getAllHiringRequests(@ParameterObject Pageable pageable) {
        Page<HiringRequest> hiringRequests = hiringService.getAllHiringRequests(pageable);
        return new ResponseEntity<>(hiringRequests, HttpStatus.OK);
    }
}

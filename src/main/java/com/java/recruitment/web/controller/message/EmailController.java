package com.java.recruitment.web.controller.message;

import com.java.recruitment.service.IEmailService;
import com.java.recruitment.web.dto.mail.MailDTO;
import com.java.recruitment.web.dto.mail.MailResponseDTO;
import lombok.RequiredArgsConstructor;
import org.simpleframework.xml.core.Validate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class EmailController {

    private final IEmailService emailService;

    @PostMapping
    public MailResponseDTO sendTextEmail(
            @RequestParam(value = "file", required = false) MultipartFile[] file,
            @RequestBody @Validate MailDTO mail
    ) {
        return emailService.sendMail(mail, file);
    }
}
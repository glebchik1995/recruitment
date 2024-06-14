package com.java.recruitment.web.controller.mail;

import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.service.IMailService;
import com.java.recruitment.web.dto.mail.MailRequest;
import com.java.recruitment.web.dto.mail.MailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Mail Controller",
        description = "CRUD OPERATIONS WITH MAIL"
)
@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
@LogInfo
public class MailController {

    private final IMailService emailService;

    @PostMapping
    @Operation(summary = "Отправить письмо")
    public MailResponse sendTextEmail(@Valid @ModelAttribute final MailRequest mail) {
        return emailService.sendMail(mail);
    }
}
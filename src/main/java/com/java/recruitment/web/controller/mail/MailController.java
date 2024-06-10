package com.java.recruitment.web.controller.mail;

import com.java.recruitment.service.IMailService;
import com.java.recruitment.web.dto.mail.MailRequest;
import com.java.recruitment.web.dto.mail.MailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class MailController {

    private final IMailService emailService;

    @PostMapping
    public MailResponse sendTextEmail(@Validated @ModelAttribute MailRequest mail) {
        return emailService.sendMail(mail);
    }
}
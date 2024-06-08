package com.java.recruitment.web.controller.message;

import com.java.recruitment.service.impl.EmailService;
import com.java.recruitment.service.model.message.MultipleReceiverRequest;
import com.java.recruitment.service.model.message.SingleReceiverRequest;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/emails")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/text")
    public void sendTextEmail(@RequestBody SingleReceiverRequest request) {
        emailService.sendTextEmail(request);
    }

    @PostMapping("/html")
    public void sendHtmlEmail(@RequestBody MultipleReceiverRequest request) throws MessagingException, UnsupportedEncodingException {
        emailService.sendHtmlEmail(request);
    }
}
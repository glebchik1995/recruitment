package com.java.recruitment.service.impl;

import com.java.recruitment.service.model.message.MultipleReceiverRequest;
import com.java.recruitment.service.model.message.SingleReceiverRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final String senderEmail;
    private final String senderText;

    public EmailService(JavaMailSender javaMailSender,
                        @Value("${spring.mail.sender.email}") String senderEmail,
                        @Value("${spring.mail.sender.text}") String senderText) {
        this.javaMailSender = javaMailSender;
        this.senderEmail = senderEmail;
        this.senderText = senderText;
    }

    public void sendTextEmail(SingleReceiverRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(request.getReceiver());
        message.setSubject("Тестовое письмо");
        message.setText("Текстовое сообщение в тестовом письме.\nВторая строка.");
        javaMailSender.send(message);
    }

    public void sendHtmlEmail(MultipleReceiverRequest request) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(new InternetAddress(senderEmail, senderText));
        helper.setTo(request.getReceivers().toArray(new String[0]));
        helper.setCc(request.getCopy());
        helper.setBcc(request.getHiddenCopy());
        helper.setSubject("Тестовое письмо");
        helper.setText("<p>Сообщение в формате <b>Html</b>.<br>Вторая строка.</p>", true);
        javaMailSender.send(message);
    }
}
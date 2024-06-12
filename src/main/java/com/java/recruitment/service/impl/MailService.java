package com.java.recruitment.service.impl;

import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.repositoty.exception.DataProcessingException;
import com.java.recruitment.service.IMailService;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.mail.MailRequest;
import com.java.recruitment.web.dto.mail.MailResponse;
import com.java.recruitment.web.mapper.MailMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MailService implements IMailService {

    private final MailMapper mailMapper;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public MailResponse sendMail(MailRequest mailRequest) {
        User sender = userRepository.findByUsername(mailRequest.getSenderMail())
                .orElseThrow(() -> new DataNotFoundException("Отправитель не найден"));

        User receiver = userRepository.findByUsername(mailRequest.getReceiverMail())
                .orElseThrow(() -> new DataNotFoundException("Получатель не найден"));

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setProtocol("smtp");
        javaMailSender.setHost("smtp." + getDomainFromEmail(sender.getUsername()));
        javaMailSender.setPort(587);
        javaMailSender.setUsername(sender.getUsername());
        javaMailSender.setPassword(mailRequest.getPassword());
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.starttls.enable", "true");
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.auth", "true");

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper
                    (
                            mimeMessage,
                            true,
                            StandardCharsets.UTF_8.name()
                    );

            prepareMailMessage(mimeMessageHelper, sender, receiver, mailRequest);

            javaMailSender.send(mimeMessage);

            MailResponse mailResponse = mailMapper.toResponse(mailRequest);
            mailResponse.setSentDate(LocalDateTime.now());
            return mailResponse;

        } catch (MessagingException e) {
            throw new MailSendException("Ошибка отправки письма", e);
        } catch (IOException e) {
            throw new DataProcessingException("Ошибка обработки файлов");
        }
    }

    private void prepareMailMessage(
            MimeMessageHelper mimeMessageHelper,
            User sender,
            User receiver,
            MailRequest mailRequest
    ) throws MessagingException, IOException {
        mimeMessageHelper.setFrom(sender.getUsername());
        mimeMessageHelper.setTo(receiver.getUsername());
        mimeMessageHelper.setSubject(mailRequest.getSubject());
        if (mailRequest.getText() != null) {
            mimeMessageHelper.setText(mailRequest.getText());
        }

        if (mailRequest.getFiles() != null) {
            addAttachments(mimeMessageHelper, mailRequest.getFiles());
        }
    }

    private void addAttachments(
            MimeMessageHelper mimeMessageHelper,
            MultipartFile[] files
    ) throws MessagingException, IOException {
        for (MultipartFile file : files) {
            if (file != null && file.getOriginalFilename() != null) {
                mimeMessageHelper.addAttachment(
                        file.getOriginalFilename(),
                        new ByteArrayResource(file.getBytes())
                );
            }
        }
    }

    private String getDomainFromEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex != -1) {
            return email.substring(atIndex + 1);
        } else {
            return "";
        }
    }
}
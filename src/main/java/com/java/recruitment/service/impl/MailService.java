package com.java.recruitment.service.impl;

import com.java.recruitment.repositoty.MailRepository;
import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.repositoty.exception.DataProcessingException;
import com.java.recruitment.service.IMailService;
import com.java.recruitment.service.model.mail.Mail;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.mail.MailDTO;
import com.java.recruitment.web.dto.mail.MailResponseDTO;
import com.java.recruitment.web.mapper.impl.MailMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService implements IMailService {

    private final MailMapper mailMapper;

    private final UserRepository userRepository;

    private final MailRepository mailRepository;

    @Override
    @Transactional
    public MailResponseDTO sendMail(MailDTO mailDTO, MultipartFile[] files) {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        User sender = userRepository.findById(mailDTO.getSenderId())
                .orElseThrow(() -> new DataNotFoundException("Отправитель не найден"));

        User receiver = userRepository.findById(mailDTO.getReceiverId())
                .orElseThrow(() -> new DataNotFoundException("Получатель не найден"));

        String protocol = "smtp";

        String host = protocol + "." + getDomainFromEmail(sender.getUsername());

        Mail mail = mailMapper.toEntity(mailDTO);
        javaMailSender.setProtocol(protocol);
        javaMailSender.setHost(host);
        javaMailSender.setPort(587);
        javaMailSender.setUsername(sender.getUsername());
        javaMailSender.setPassword(mailDTO.getPassword());
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.starttls.enable", "true");
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.auth", "true");

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                    mimeMessage,
                    true,
                    "UTF-8"
            );

            mimeMessageHelper.setFrom(sender.getUsername());
            mimeMessageHelper.setTo(receiver.getUsername());
            mimeMessageHelper.setSubject(mailDTO.getSubject());
            if (mailDTO.getText() != null) {
                mimeMessageHelper.setText(mailDTO.getText());
            }

            if (files != null) {
                for (MultipartFile file : files) {
                    if (file.getOriginalFilename() != null) {
                        mimeMessageHelper.addAttachment(
                                file.getOriginalFilename(),
                                new ByteArrayResource(file.getBytes()));
                    }
                }
            }

            javaMailSender.send(mimeMessage);

            mail.setSentDate(LocalDateTime.now());
            mailRepository.save(mail);

            MailResponseDTO mailResponseDTO = mailMapper.toDto(mail);
            mailResponseDTO.setSenderMail(sender.getUsername());
            mailResponseDTO.setReceiverMail(receiver.getUsername());
            return mailResponseDTO;

        } catch (MessagingException e) {
            log.error("Ошибка отправки письма", e);
            throw new MailSendException("Ошибка отправки письма", e);
        } catch (IOException e) {
            log.error("Ошибка обработки файлов", e);
            throw new DataProcessingException("Ошибка обработки файлов");
        }
    }

    public String getDomainFromEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex != -1) {
            return email.substring(atIndex + 1);
        } else {
            return "";
        }
    }

}
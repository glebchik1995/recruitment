package com.java.recruitment.service;

import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.impl.MailService;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.mail.MailRequest;
import com.java.recruitment.web.mapper.MailMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MailMapper mailMapper;

    @InjectMocks
    private MailService mailService;

    @Mock
    private JavaMailSenderImpl javaMailSender;

//    @Test
//    void sendEmailForRegistration() {
//        try {
//            String name = "Mike";
//            String username = "mike@gmail.com";
//            User user = new User();
//            user.setName(name);
//            user.setUsername(username);
//            Mockito.when(javaMailSender.createMimeMessage())
//                    .thenReturn(Mockito.mock(MimeMessage.class));
//            Mockito.when(configuration.getTemplate("register.ftlh"))
//                    .thenReturn(Mockito.mock(Template.class));
//            mailService.sendEmail(user, MailType.REGISTRATION, new Properties());
//            Mockito.verify(configuration).getTemplate("register.ftlh");
//            Mockito.verify(javaMailSender).send(Mockito.any(MimeMessage.class));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }



    @Test
    public void sendMail_withInvalidSenderMail_shouldThrowDataNotFoundException() {
        MailRequest mailRequest = new MailRequest("invalidSenderMail", "receiverMail", "password", "text", null, "password");

        when(userRepository.findByUsername("invalidSenderMail")).thenReturn(java.util.Optional.empty());

        assertThrows(DataNotFoundException.class, () -> mailService.sendMail(mailRequest));
    }

    @Test
    public void sendMail_withInvalidReceiverMail_shouldThrowDataNotFoundException() {
        MailRequest mailRequest = new MailRequest("senderMail", "invalidReceiverMail", "password", "subject", null, "password");

        when(userRepository.findByUsername("senderMail")).thenReturn(java.util.Optional.of(new User()));
        when(userRepository.findByUsername("invalidReceiverMail")).thenReturn(java.util.Optional.empty());

        assertThrows(DataNotFoundException.class, () -> mailService.sendMail(mailRequest));
    }

}

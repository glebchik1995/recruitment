package com.java.recruitment.service;

import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.service.impl.ChatMessageService;
import com.java.recruitment.web.mapper.MailMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MailMapper mailMapper;

    @InjectMocks
    private ChatMessageService mailService;

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
//            Mockito.when(configuration.getTemplate("message.ftlh"))
//                    .thenReturn(Mockito.mock(Template.class));
//            mailService.sendEmail(user, MailType.REGISTRATION, new Properties());
//            Mockito.verify(configuration).getTemplate("message.ftlh");
//            Mockito.verify(javaMailSender).send(Mockito.any(MimeMessage.class));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }



//    @Test
//    public void sendMail_withInvalidSenderMail_shouldThrowDataNotFoundException() {
//        MailRequest mailRequest = new MailRequest("invalidSenderMail", "receiverMail", "password", "text", null, "password");
//
//        when(userRepository.findByUsername("invalidSenderMail")).thenReturn(java.util.Optional.empty());
//
//        assertThrows(DataNotFoundException.class, () -> mailService.sendMail(mailRequest));
//    }
//
//    @Test
//    public void sendMail_withInvalidReceiverMail_shouldThrowDataNotFoundException() {
//        MailRequest mailRequest = new MailRequest("senderMail", "invalidReceiverMail", "password", "subject", null, "password");
//
//        when(userRepository.findByUsername("senderMail")).thenReturn(java.util.Optional.of(new User()));
//        when(userRepository.findByUsername("invalidReceiverMail")).thenReturn(java.util.Optional.empty());
//
//        assertThrows(DataNotFoundException.class, () -> mailService.sendMail(mailRequest));
//    }

}

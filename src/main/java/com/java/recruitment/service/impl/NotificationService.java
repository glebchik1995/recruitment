package com.java.recruitment.service.impl;

import com.java.recruitment.service.INotificationService;
import com.java.recruitment.service.model.chat.NotificationType;
import com.java.recruitment.service.model.user.User;
import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {

    private final Configuration configuration;

    private final JavaMailSender mailSender;

    public void sendNotification(
            final User receiver,
            final NotificationType type,
            final Properties params

    ) {
        switch (type) {
            case NEW_MESSAGE -> sendNotificationAboutNewMessage(
                    receiver,
                    params
            );

            case NEW_JOB_REQUEST -> sendNotificationAboutNewJobRequest(
                    receiver,
                    params
            );
            default -> {
            }
        }
    }

    @SneakyThrows
    private void sendNotificationAboutNewMessage(
            final User receiver,
            final Properties params
    ) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                false,
                "UTF-8"
        );

        helper.setSubject("Уведомление о новом сообщении, " + receiver.getName());
        helper.setTo(receiver.getUsername());
        String emailContent = getContentWithNotificationAboutNewMessage(receiver, params);
        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);
    }

    @SneakyThrows
    private void sendNotificationAboutNewJobRequest(
            final User receiver,
            final Properties params
    ) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                false,
                "UTF-8"
        );

        helper.setSubject("Уведомление о новой заявки на найм, " + receiver.getName());
        helper.setTo(receiver.getUsername());
        String emailContent = getContentWithNotificationAboutNewJobRequest(receiver, params);
        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);
    }


    @SneakyThrows
    private String getContentWithNotificationAboutNewMessage(
            final User receiver,
            final Properties properties
    ) {
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", receiver.getName());
        configuration.getTemplate("message.ftlh")
                .process(model, writer);
        return writer.getBuffer().toString();
    }

    @SneakyThrows
    private String getContentWithNotificationAboutNewJobRequest(
            final User receiver,
            final Properties properties
    ) {
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", receiver.getName());
        model.put("title", properties.getProperty("vacancy.title"));
        model.put("position", properties.getProperty("vacancy.position"));
        configuration.getTemplate("job-request.ftlh")
                .process(model, writer);
        return writer.getBuffer().toString();
    }
}

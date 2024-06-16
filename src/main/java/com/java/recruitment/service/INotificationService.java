package com.java.recruitment.service;

import com.java.recruitment.service.model.chat.NotificationType;
import com.java.recruitment.service.model.user.User;

import java.util.Properties;

public interface INotificationService {

    void sendNotification(
            User receiver,
            NotificationType type,
            Properties params

    );
}

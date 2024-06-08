package com.java.recruitment.service;

import jakarta.mail.MessagingException;

import java.io.FileNotFoundException;

public interface IEmailService {

    void sendSimpleEmail(final String toAddress, final String subject, final String message);
    void sendEmailWithAttachment
            (final String toAddress,
                                 final String subject,
                                 final String message,
                                 String attachment
            ) throws MessagingException, FileNotFoundException, MessagingException, FileNotFoundException;
}

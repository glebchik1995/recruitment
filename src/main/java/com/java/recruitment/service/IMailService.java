package com.java.recruitment.service;

import com.java.recruitment.web.dto.mail.MailRequest;
import com.java.recruitment.web.dto.mail.MailResponse;

public interface IMailService {

    MailResponse sendMail(MailRequest file);
}

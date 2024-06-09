package com.java.recruitment.service;

import com.java.recruitment.web.dto.mail.MailDTO;
import com.java.recruitment.web.dto.mail.MailResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IEmailService {

    MailResponseDTO sendMail(MailDTO file, MultipartFile[] mail);
}

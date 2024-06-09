package com.java.recruitment.repositoty;

import com.java.recruitment.service.model.mail.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<Mail, Long> {
}

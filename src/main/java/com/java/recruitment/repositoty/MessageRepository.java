package com.java.recruitment.repositoty;

import com.java.recruitment.service.model.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}

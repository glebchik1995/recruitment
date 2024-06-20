package com.java.recruitment.repositoty;

import com.java.recruitment.service.model.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChatRepository extends JpaRepository<ChatMessage, Long>, JpaSpecificationExecutor<ChatMessage> {
}

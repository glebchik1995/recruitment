package com.java.recruitment.repositoty;

import com.java.recruitment.service.model.message.ChatMessage;
import com.java.recruitment.service.model.message.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, String> {

    long countBySenderIdAndRecipientIdAndStatus(
            String senderId,
            String recipientId,
            MessageStatus status
    );

    List<ChatMessage> findByChatId(String chatId);
}

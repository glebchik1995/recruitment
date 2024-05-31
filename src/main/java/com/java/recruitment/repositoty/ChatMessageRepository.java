package com.java.recruitment.repositoty;

import com.java.recruitment.service.model.message.ChatMessage;
import com.java.recruitment.service.model.message.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository
        extends MongoRepository<ChatMessage, String> {

    long countBySenderIdAndRecipientIdAndStatus(
            String senderId,
            String recipientId,
            MessageStatus status
    );

    List<ChatMessage> findByChatId(String chatId);
}

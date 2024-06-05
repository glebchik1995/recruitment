package com.java.recruitment.service;

import com.java.recruitment.service.model.message.ChatMessage;

import java.util.List;

public interface IChatMessageService {

    ChatMessage save(ChatMessage chatMessage);

    long countNewMessages(String senderId, String recipientId);

    List<ChatMessage> findChatMessages(String senderId, String recipientId);

    ChatMessage findById(String id);
}

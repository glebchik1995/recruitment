package com.java.recruitment.service;

import com.java.recruitment.service.filter.JoinType;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.chat.ChatMessageRequestDTO;
import com.java.recruitment.web.dto.chat.ChatMessageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IChatMessageService {

    ChatMessageResponseDTO sendMessage(ChatMessageRequestDTO request, Long senderId);

    ChatMessageResponseDTO getChatMessageById(User currentUser, Long chatMessageId);

    Page<ChatMessageResponseDTO> getFilteredChatMessages(
            Long currentUserId,
            Long otherId,
            String criteriaJson,
            JoinType joinType,
            Pageable pageable
    );
}
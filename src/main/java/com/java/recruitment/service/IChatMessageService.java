package com.java.recruitment.service;

import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.chat.ChatMessageRequestDTO;
import com.java.recruitment.web.dto.chat.ChatMessageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IChatMessageService {

    ChatMessageResponseDTO sendMessage(ChatMessageRequestDTO request, User sender);

    ChatMessageResponseDTO getChatMessageById(User currentUser, Long chatMessageId);

    Page<ChatMessageResponseDTO> getFilteredChatMessages(
            List<CriteriaModel> criteriaModelList,
            Long currentUserId,
            Long otherUserId,
            Pageable pageable
    );
}
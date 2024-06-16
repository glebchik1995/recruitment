package com.java.recruitment.service;

import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.web.dto.chat.ChatMessageRequestDTO;
import com.java.recruitment.web.dto.chat.ChatMessageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IChatMessageService {

    ChatMessageResponseDTO sendMessage(ChatMessageRequestDTO request, Long senderId);

    ChatMessageResponseDTO getMessageById(Long id);

    Page<ChatMessageResponseDTO> getAllMessagesWithCriteria(
            List<CriteriaModel> criteriaModelList,
            Long recruiter_id,
            Pageable pageable);

    boolean isRecruiterForMessage(
            Long userId,
            Long messageId
    );

    boolean isHrForMessage(
            Long userId,
            Long messageId
    );
}

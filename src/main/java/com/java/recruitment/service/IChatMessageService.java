package com.java.recruitment.service;

import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.model.chat.NotificationType;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.mail.MailRequestDTO;
import com.java.recruitment.web.dto.mail.MailResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Properties;

public interface IChatMessageService {

    MailResponseDTO sendMessage(MailRequestDTO request);

    MailResponseDTO getMessageById(Long id);

    Page<MailResponseDTO> getAllMessagesWithCriteria(
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

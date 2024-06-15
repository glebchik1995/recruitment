package com.java.recruitment.service.impl;

import com.java.recruitment.aspect.log.LogError;
import com.java.recruitment.repositoty.MailRepository;
import com.java.recruitment.service.IChatMessageService;
import com.java.recruitment.service.INotificationService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.filter.GenericSpecification;
import com.java.recruitment.service.model.chat.ChatMessage;
import com.java.recruitment.web.dto.mail.MailRequestDTO;
import com.java.recruitment.web.dto.mail.MailResponseDTO;
import com.java.recruitment.web.mapper.MailMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Properties;

import static com.java.recruitment.service.model.chat.NotificationType.NEW_MESSAGE;

@Service
@LogError
@RequiredArgsConstructor
public class ChatMessageService implements IChatMessageService {

    private final MailMapper mailMapper;

    private final MailRepository mailRepository;

    private final INotificationService notificationService;

    @Override
    @Transactional
    public MailResponseDTO sendMessage(MailRequestDTO request) {
        Properties properties = new Properties();
        ChatMessage message = mailMapper.toEntity(request);
        notificationService.sendNotification(
                message.getReceiver(),
                NEW_MESSAGE,
                properties
        );
        message.setSentDate(LocalDate.now());
        message.setSentTime(LocalTime.now());
        ChatMessage saveMail = mailRepository.save(message);
        return mailMapper.toDto(saveMail);
    }

    @Override
    public MailResponseDTO getMessageById(Long id) {
        return null;
    }

    @Override
    public Page<MailResponseDTO> getAllMessagesWithCriteria(
            List<CriteriaModel> criteriaModelList,
            Long recruiter_id,
            Pageable pageable) {
        Specification<ChatMessage> specification
                = new GenericSpecification<>(criteriaModelList, ChatMessage.class);
        Page<ChatMessage> jobRequests = mailRepository.findMessageForRecruiter(
                recruiter_id,
                specification,
                pageable
        );
        return jobRequests.map(jobRequestMapper::toDto);
    }

    @Override
    public boolean isRecruiterForMessage(Long userId, Long messageId) {
        return false;
    }

    @Override
    public boolean isHrForMessage(Long userId, Long messageId) {
        return false;
    }
}
package com.java.recruitment.service.impl;

import com.java.recruitment.aspect.log.LogError;
import com.java.recruitment.repositoty.ChatRepository;
import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.repositoty.exception.DataAccessException;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.IChatMessageService;
import com.java.recruitment.service.INotificationService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.filter.GenericSpecification;
import com.java.recruitment.service.model.chat.ChatMessage;
import com.java.recruitment.service.model.user.Role;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.chat.ChatMessageRequestDTO;
import com.java.recruitment.web.dto.chat.ChatMessageResponseDTO;
import com.java.recruitment.web.mapper.ChatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Properties;

import static com.java.recruitment.service.model.chat.NotificationType.NEW_MESSAGE;

@Service
@LogError
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService implements IChatMessageService {

    private final ChatMapper chatMapper;

    private final ChatRepository chatRepository;

    private final INotificationService notificationService;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public ChatMessageResponseDTO sendMessage(
            final ChatMessageRequestDTO request,
            final User sender
    ) {

        User recipient = userRepository.findById(request.getRecipientId())
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));

        if (recipient.getRole().equals(sender.getRole()) || recipient.getRole().equals(Role.ADMIN)) {
            throw new DataAccessException("Вы не можете отправить сообщение этому пользователю");
        }

        Properties properties = new Properties();

        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .recipient(recipient)
                .text(request.getText())
                .build();

        notificationService.sendNotification(
                message.getRecipient(),
                NEW_MESSAGE,
                properties
        );
        message.setSentDate(LocalDate.now());
        message.setSentTime(LocalTime.now());
        ChatMessage saveMail = chatRepository.save(message);
        return chatMapper.toDTO(saveMail);
    }

    @Override
    public ChatMessageResponseDTO getChatMessageById(
            final User currentUser,
            final Long chatMessageId
    ) {
        ChatMessage chatMessage =
                switch (currentUser.getRole()) {
            case HR -> chatRepository.findChatMessageByIdAndUserHrId(chatMessageId, chatMessageId);
            case RECRUITER -> chatRepository.findChatMessageByIdAndUserRecruiterId(chatMessageId, chatMessageId);
            default -> chatRepository.findById(chatMessageId)
                    .orElseThrow(() -> new DataNotFoundException("Сообщение в чате не найдено"));
        };
        if (chatMessage != null) {
            return chatMapper.toDTO(chatMessage);
        } else {
            throw new DataNotFoundException("Сообщение в чате не найдено");
        }
    }

    @Override
    public Page<ChatMessageResponseDTO> getFilteredChatMessages(
            final List<CriteriaModel> criteriaModelList,
            final Long currentUserId,
            final Long otherUserId,
            final Pageable pageable
    ) {

        Specification<ChatMessage> specification =
                new GenericSpecification<>(criteriaModelList, ChatMessage.class);

        Page<ChatMessage> jobRequests = chatRepository.findAllBySenderIdAndRecipientIdOrRecipientIdAndSenderId(
                currentUserId,
                otherUserId,
                currentUserId,
                otherUserId,
                pageable,
                specification
        );
        return jobRequests.map(chatMapper::toDTO);
    }
}
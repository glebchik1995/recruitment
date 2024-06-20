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
import com.java.recruitment.service.filter.JoinType;
import com.java.recruitment.service.model.chat.ChatMessage;
import com.java.recruitment.service.model.user.Role;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.util.FilterParser;
import com.java.recruitment.web.dto.chat.ChatMessageRequestDTO;
import com.java.recruitment.web.dto.chat.ChatMessageResponseDTO;
import com.java.recruitment.web.mapper.ChatMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            final Long senderId
    ) {

        User recipient = userRepository.findById(request.getRecipientId())
                .orElseThrow(() -> new DataNotFoundException("Получатель не найден"));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new DataNotFoundException("Отправитель не найден"));

        if (recipient.getRole().equals(sender.getRole()) || recipient.getRole().equals(Role.ADMIN)) {
            throw new DataAccessException("Вы не можете отправить сообщение этому пользователю");
        }

        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .recipient(recipient)
                .text(request.getText())
                .build();

        notificationService.sendNotification(
                message.getRecipient(),
                NEW_MESSAGE,
                new Properties()
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

    @SneakyThrows
    @Override
    public Page<ChatMessageResponseDTO> getFilteredChatMessages(
            final Long userId,
            final Long otherId,
            final String criteriaJson,
            final JoinType joinType,
            final Pageable pageable
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));

        List<CriteriaModel> criteriaList = FilterParser.parseCriteriaJson(criteriaJson);

        switch (user.getRole()) {
            case RECRUITER:
            case HR:
                if (!criteriaList.isEmpty()) {
                    return chatRepository.findAllBySenderIdAndRecipientId(
                            user.getId(),
                            otherId,
                            new GenericSpecification<>(
                                    criteriaList,
                                    joinType,
                                    ChatMessage.class
                            ),
                            pageable
                    ).map(chatMapper::toDTO);
                }
            case ADMIN:
                if (!criteriaList.isEmpty()) {
                    return chatRepository.findAll(
                            new GenericSpecification<>(
                                    criteriaList,
                                    joinType,
                                    ChatMessage.class
                            ),
                            pageable
                    ).map(chatMapper::toDTO);
                }

            default:
                return chatRepository.findAll(pageable).map(chatMapper::toDTO);
        }
    }
}
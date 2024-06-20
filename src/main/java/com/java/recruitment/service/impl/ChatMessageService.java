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
import com.java.recruitment.service.model.chat.ChatMessage_;
import com.java.recruitment.service.model.chat.Sender;
import com.java.recruitment.service.model.user.Role;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.service.model.user.User_;
import com.java.recruitment.util.FilterParser;
import com.java.recruitment.web.dto.chat.ChatMessageRequestDTO;
import com.java.recruitment.web.dto.chat.ChatMessageResponseDTO;
import com.java.recruitment.web.mapper.ChatMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    private final EntityManager entityManager;

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

        LocalDateTime dateTime = LocalDateTime.now();

        if (
                recipient.getRole().equals(
                        sender.getRole()
                ) || recipient.getRole().equals(
                        Role.ADMIN
                ) || sender.getRole().equals(
                        Role.ADMIN
                )
        ) {
            throw new DataAccessException("Вы не можете отправить сообщение этому пользователю");
        }

        ChatMessage message;

        switch (sender.getRole()) {
            case HR:
                message = ChatMessage.builder()
                        .hr(sender)
                        .recruiter(recipient)
                        .sender(Sender.HR)
                        .text(request.getText())
                        .build();
                notificationService.sendNotification(
                        recipient,
                        NEW_MESSAGE,
                        new Properties()
                );
                break;
            case RECRUITER:
                message = ChatMessage.builder()
                        .hr(recipient)
                        .recruiter(sender)
                        .sender(Sender.RECRUITER)
                        .text(request.getText())
                        .build();
                notificationService.sendNotification(
                        recipient,
                        NEW_MESSAGE,
                        new Properties()
                );
                break;
            default:
                throw new DataAccessException("Сообщения могут отправляться только HR и Рекрутер.");
        }

        message.setSentDate(dateTime.toLocalDate());
        message.setSentTime(dateTime.toLocalTime());

        ChatMessage savedMessage = chatRepository.save(message);

        return chatMapper.toDTO(savedMessage);
    }

    @Override
    public ChatMessageResponseDTO getChatMessageById(
            final Long userId,
            final Long chatMessageId
    ) {
        ChatMessage message = findChatMessage(
                userId,
                chatMessageId
        );
        return chatMapper.toDTO(message);
    }

    @SneakyThrows
    @Override
    public Page<ChatMessageResponseDTO> getFilteredChatMessages(
            final Long userId,
            final String criteriaJson,
            final JoinType joinType,
            final Pageable pageable
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));

        List<CriteriaModel> criteriaList = List.of();

        if (criteriaJson != null) {
            criteriaList = FilterParser.parseCriteriaJson(criteriaJson);
        }
        Specification<ChatMessage> userPredicate;

        return switch (user.getRole()) {
            case ADMIN -> criteriaList.isEmpty()
                    ? chatRepository.findAll(pageable).map(chatMapper::toDTO)
                    : chatRepository.findAll(
                    new GenericSpecification<>(
                            criteriaList,
                            joinType,
                            ChatMessage.class
                    ),
                    pageable
            ).map(chatMapper::toDTO);
            case HR -> {
                userPredicate = (root, query, cb) ->
                        cb.equal(root.get(ChatMessage_.hr).get(User_.id), userId);
                yield criteriaList.isEmpty()
                        ? chatRepository.findAll(userPredicate, pageable).map(chatMapper::toDTO)
                        : chatRepository.findAll(
                        new GenericSpecification<>(
                                criteriaList,
                                joinType,
                                ChatMessage.class
                        ).and(userPredicate),
                        pageable
                ).map(chatMapper::toDTO);
            }
            case RECRUITER -> {
                userPredicate = (root, query, cb) ->
                        cb.equal(root.get(ChatMessage_.recruiter).get(User_.id), userId);
                yield criteriaList.isEmpty()
                        ? chatRepository.findAll(userPredicate, pageable).map(chatMapper::toDTO)
                        : chatRepository.findAll(
                        new GenericSpecification<>(
                                criteriaList,
                                joinType,
                                ChatMessage.class
                        ).and(userPredicate),
                        pageable
                ).map(chatMapper::toDTO);
            }
        };
    }

    private ChatMessage findChatMessage(
            final Long userId,
            final Long chatMessageId
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найдено"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ChatMessage> query = cb.createQuery(ChatMessage.class);
        Root<ChatMessage> root = query.from(ChatMessage.class);
        Predicate chatMessagePredicate = cb.equal(root.get(ChatMessage_.id), chatMessageId);

        switch (user.getRole()) {
            case ADMIN:
                query.where(chatMessagePredicate);
                try {
                    return entityManager.createQuery(query).getSingleResult();
                } catch (NoResultException e) {
                    throw new DataNotFoundException("Сообщение не найдено");
                }
            case HR:
                Predicate hrPredicate = cb.equal(root.get(ChatMessage_.hr).get(User_.id), userId);
                Predicate finalHrPredicate = cb.and(hrPredicate, chatMessagePredicate);
                query.where(finalHrPredicate);
                try {
                    return entityManager.createQuery(query).getSingleResult();
                } catch (NoResultException e) {
                    throw new DataNotFoundException("Сообщение не найдено");
                }
            case RECRUITER:
                Predicate recruiterPredicate = cb.equal(root.get(ChatMessage_.recruiter).get(User_.id), userId);
                Predicate finalRecruiterPredicate = cb.and(recruiterPredicate, chatMessagePredicate);
                query.where(finalRecruiterPredicate);
                try {
                    return entityManager.createQuery(query).getSingleResult();
                } catch (NoResultException e) {
                    throw new DataNotFoundException("Сообщение не найдено");
                }
            default:
                throw new DataNotFoundException("Роль пользователя не поддерживается");
        }
    }
}
package com.java.recruitment.web.controller.chat;

import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.repositoty.ChatRepository;
import com.java.recruitment.service.IChatMessageService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.util.FilterParser;
import com.java.recruitment.web.dto.chat.ChatMessageRequestDTO;
import com.java.recruitment.web.dto.chat.ChatMessageResponseDTO;
import com.java.recruitment.web.mapper.ChatMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Chat Controller",
        description = "CRUD OPERATIONS WITH MAIL"
)
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@LogInfo
@Validated
public class ChatController {

    private final IChatMessageService emailService;

    private final ChatMapper mapper;

    private final ChatRepository mailRepository;

    @PostMapping
    @Operation(summary = "Отправить сообщение")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatMessageResponseDTO sendMessage(
            @AuthenticationPrincipal User currentUser,
            @RequestBody @Valid final ChatMessageRequestDTO dto
    ) {

        return emailService.sendMessage(dto, currentUser);
    }

    @GetMapping("/{otherUserId}")
    @Operation(summary = "Получить все заявки")
    public Page<ChatMessageResponseDTO> getChatMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable @Min(1) Long otherUserId,
            @RequestParam(required = false) final String criteriaJson,
            @ParameterObject Pageable pageable)
            throws BadRequestException {
        List<CriteriaModel> criteriaList;
        if (criteriaJson != null) {
            criteriaList = FilterParser.parseCriteriaJson(criteriaJson);
            return emailService.getFilteredChatMessages(
                    criteriaList,
                    currentUser.getId(),
                    otherUserId,
                    pageable
            );
        } else {
            return mailRepository.findAllBySenderIdAndRecipientIdOrRecipientIdAndSenderId(
                    currentUser.getId(),
                    otherUserId,
                    otherUserId,
                    currentUser.getId(),
                    pageable
            ).map(mapper::toDTO);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить заявку по ID")
    public ChatMessageResponseDTO getChatMessageById(
            @AuthenticationPrincipal User currentUser,
            @PathVariable @Min(1) final Long id) {
        return emailService.getChatMessageById(currentUser, id);
    }
}
package com.java.recruitment.web.controller.chat;

import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.service.IChatMessageService;
import com.java.recruitment.service.filter.JoinType;
import com.java.recruitment.validation.line.ValidCriteriaJson;
import com.java.recruitment.web.dto.chat.ChatMessageRequestDTO;
import com.java.recruitment.web.dto.chat.ChatMessageResponseDTO;
import com.java.recruitment.web.security.JwtEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @Operation(summary = "Отправить сообщение")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatMessageResponseDTO sendMessage(
            @AuthenticationPrincipal final JwtEntity sender,
            @RequestBody @Valid final ChatMessageRequestDTO dto
    ) {

        return emailService.sendMessage(dto, sender.getId());
    }

    @GetMapping
    @Operation(summary = "Получить все заявки")
    public Page<ChatMessageResponseDTO> getChatMessages(
            @AuthenticationPrincipal final JwtEntity currentUser,
            @RequestParam(required = false) @ValidCriteriaJson final String criteriaJson,
            @RequestParam(required = false) final JoinType joinType,
            @ParameterObject Pageable pageable
    ) {
        return emailService.getFilteredChatMessages(
                currentUser.getId(),
                criteriaJson,
                joinType,
                pageable
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить заявку по ID")
    public ChatMessageResponseDTO getChatMessageById(
            @AuthenticationPrincipal final JwtEntity currentUser,
            @PathVariable @Min(1) final Long id) {
        return emailService.getChatMessageById(
                currentUser.getId(),
                id
        );
    }
}
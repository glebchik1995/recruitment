package com.java.recruitment.web.controller.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.repositoty.ChatRepository;
import com.java.recruitment.service.IChatMessageService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.web.dto.chat.ChatMessageRequestDTO;
import com.java.recruitment.web.dto.chat.ChatMessageResponseDTO;
import com.java.recruitment.web.mapper.ChatMapper;
import com.java.recruitment.web.security.expression.CustomSecurityExpression;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Tag(
        name = "Chat Controller",
        description = "CRUD OPERATIONS WITH MAIL"
)
@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
@LogInfo
public class ChatController {

    private final IChatMessageService emailService;

    private final ChatMapper mapper;

    private final CustomSecurityExpression expression;

    private final ChatRepository mailRepository;

    @PostMapping
    @Operation(summary = "Отправить сообщение")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatMessageResponseDTO sendMessage(
            @RequestBody @Valid final ChatMessageRequestDTO dto
    ) {
        Long senderId = expression.getIdFromContext();

        return emailService.sendMessage(dto, senderId);
    }

    @GetMapping
    @Operation(summary = "Получить все заявки")
    public Page<ChatMessageResponseDTO> getAllMessages(
            @RequestParam(required = false) final String criteriaJson,
            @ParameterObject Pageable pageable)
            throws BadRequestException {

        Long recruiter_id = expression.getIdFromContext();

        List<CriteriaModel> criteriaList;

        ObjectMapper objectMapper = new ObjectMapper();
        if (criteriaJson != null) {
            try {
                criteriaList = Arrays.asList(
                        objectMapper.readValue(
                                criteriaJson,
                                CriteriaModel[].class
                        )
                );

                return emailService.getAllMessagesWithCriteria(
                        criteriaList,
                        recruiter_id,
                        pageable
                );
            } catch (IOException ex) {
                throw new BadRequestException("Не удалось проанализировать условия", ex);
            }
        } else {
            return mailRepository.findAllMessageForRecruiter(
                            recruiter_id,
                            pageable
                    )
                    .map(mapper::toDTO);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить заявку по ID")
    @PreAuthorize("@cse.canAccessMessageForRecruiter(#id) || @cse.canAccessMessageForHr(#id)")
    public ChatMessageResponseDTO getMessageById(@PathVariable @Min(1) final Long id) {
        return emailService.getMessageById(id);
    }
}
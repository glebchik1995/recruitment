package com.java.recruitment.web.controller.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.repositoty.MailRepository;
import com.java.recruitment.service.IChatMessageService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.web.dto.mail.MailRequestDTO;
import com.java.recruitment.web.dto.mail.MailResponseDTO;
import com.java.recruitment.web.mapper.MailMapper;
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
        name = "Mail Controller",
        description = "CRUD OPERATIONS WITH MAIL"
)
@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
@LogInfo
public class ChatController {

    private final IChatMessageService emailService;

    private final MailMapper mapper;

    private final CustomSecurityExpression expression;

    private final MailRepository mailRepository;

    @PostMapping
    @Operation(summary = "Отправить сообщение")
    @ResponseStatus(HttpStatus.CREATED)
    public MailResponseDTO sendMessage(@RequestBody @Valid final MailRequestDTO mail) {
        return emailService.sendMessage(mail);
    }

    @GetMapping
    @Operation(summary = "Получить все заявки")
    public Page<MailResponseDTO> getAllMessages(
            @RequestParam(required = false) String criteriaJson,
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
            return mailRepository.getMessagesForRecruiter(
                            recruiter_id,
                            pageable
                    )
                    .map(mapper::toDto);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить заявку по ID")
    @PreAuthorize("@cse.canAccessMessageForRecruiter(#id) || @cse.canAccessMessageForHr(#id)")
    public MailResponseDTO getMessageById(@PathVariable @Min(1) Long id) {
        return emailService.getMessageById(id);
    }
}
package com.java.recruitment.web.mapper;

import com.java.recruitment.service.model.chat.ChatMessage;
import com.java.recruitment.web.dto.mail.MailRequestDTO;
import com.java.recruitment.web.dto.mail.MailResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MailMapper {

    @Mapping(target = "sentDate", ignore = true) // игнорирование sendDate, поскольку его нет в MailRequest
    MailResponseDTO toDto(ChatMessage mail);

    ChatMessage toEntity(MailRequestDTO mailRequest);
}

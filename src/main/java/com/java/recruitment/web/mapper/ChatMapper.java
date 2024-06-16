package com.java.recruitment.web.mapper;

import com.java.recruitment.service.model.chat.ChatMessage;
import com.java.recruitment.web.dto.chat.ChatMessageResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ChatMapper {

    ChatMessageResponseDTO toDTO(ChatMessage mail);

}

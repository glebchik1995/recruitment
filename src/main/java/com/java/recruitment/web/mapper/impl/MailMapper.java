package com.java.recruitment.web.mapper.impl;

import com.java.recruitment.service.model.mail.Mail;
import com.java.recruitment.web.dto.mail.MailDTO;
import com.java.recruitment.web.dto.mail.MailResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MailMapper {

    @Mapping(source = "sender.username", target = "senderMail")
    @Mapping(source = "receiver.username", target = "receiverMail")
    MailResponseDTO toDto(Mail mail);

    @Mapping(source = "sender_id", target = "sender.id")
    @Mapping(source = "receiver_id", target = "receiver.id")
    Mail toEntity(MailDTO mailDTO);
}

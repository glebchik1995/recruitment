package com.java.recruitment.web.mapper;

import com.java.recruitment.web.dto.mail.MailRequest;
import com.java.recruitment.web.dto.mail.MailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MailMapper {

    @Mapping(target = "sentDate", ignore = true) // игнорирование sendDate, поскольку его нет в MailRequest
    MailResponse toResponse(MailRequest mailRequest);
}

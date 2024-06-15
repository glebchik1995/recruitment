package com.java.recruitment.web.dto.mail;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.java.recruitment.service.model.chat.NotificationType;
import com.java.recruitment.web.dto.user.UserDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MailResponseDTO {

    private UserDTO sender;

    private UserDTO receiver;

    private String subject;

    private NotificationType type;

    private String text;

    @Schema(description = "Дата отправления")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    private LocalDateTime sentDate;
}

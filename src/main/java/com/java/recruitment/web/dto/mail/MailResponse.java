package com.java.recruitment.web.dto.mail;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MailResponse {

    @Schema(description = "Почта отправителя", example = "example@gmail.com")
    private String senderMail;

    @Schema(description = "Почта получателя", example = "example@gmail.com")
    private String receiverMail;

    @Schema(description = "Тема письма")
    private String subject;

    @Schema(description = "Текст письма")
    private String text;

    @Schema(description = "Дата отправления")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    private LocalDateTime sentDate;
}

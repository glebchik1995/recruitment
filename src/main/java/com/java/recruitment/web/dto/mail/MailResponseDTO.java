package com.java.recruitment.web.dto.mail;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "MailResponse DTO")
public class MailResponseDTO {

    private Long id;

    private String senderMail;

    private String receiverMail;

    private String subject;

    private String text;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    private LocalDateTime sentDate;
}

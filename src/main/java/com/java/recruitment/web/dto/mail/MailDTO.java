package com.java.recruitment.web.dto.mail;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.annotation.Nullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Mail DTO")
public class MailDTO {

    @Schema(description = "ID Mail", example = "1")
    private Long id;

    @Schema(description = "ID отправителя", example = "1")
    @NotNull(message = "ID отправителя не должно быть null.")
    private Long sender_id;

    @Schema(description = "ID получателя", example = "1")
    @NotNull(message = "ID получателя не должно быть null.")
    private Long receiver_id;

    @Schema(description = "Тема письма")
    @NotNull(message = "Тема письма не должена быть null.")
    @Length(min = 1, max = 55, message = "размер темы должен быть от 1 до 1000 символов.")
    private String subject;

    @Schema(description = "Текст письма")
    @Nullable
    private String text;
}

package com.java.recruitment.web.dto.mail;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MailRequest {

    @Schema(description = "Email отправителя", example = "1")
    @Email
    private String senderMail;

    @Schema(description = "Email получателя", example = "1")
    @Email
    private String receiverMail;

    @Schema(description = "Тема письма")
    @NotNull(message = "Тема письма не должна быть null.")
    @Length(min = 1, max = 55, message = "размер темы должен быть от 1 до 1000 символов.")
    private String subject;

    @Schema(description = "Текст письма")
    @Nullable
    private String text;

    @Schema(description = "Вложенные файлы")
    @Nullable
    private MultipartFile[] files;

    @Schema(description = "Пароль пользователя от почты.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Пароль не должен быть null.")
    private String password;
}

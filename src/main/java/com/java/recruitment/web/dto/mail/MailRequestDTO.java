package com.java.recruitment.web.dto.mail;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.annotation.Nullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MailRequestDTO {

    private Long senderId;

    private Long receiverId;

    @Nullable
    private String text;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Пароль не должен быть null.")
    private String password;
}

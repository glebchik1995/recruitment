package com.java.recruitment.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "RЗапрос для авторизации")
public class JwtRequest {

    @Schema(description = "email", example = "johndoe@gmail.com")
    @Email
    private String username;

    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Length(min = 5, max = 255, message = "Длина пароля должна быть не более 255 символов")
    @NotBlank(message = "Пароль не может быть пустыми")
    private String password;

}
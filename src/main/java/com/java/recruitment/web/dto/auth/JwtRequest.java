package com.java.recruitment.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Schema(description = "Request for login")
public class JwtRequest {

    @Schema(description = "email", example = "johndoe@gmail.com")
    @NotNull
    @Email
    private String username;

    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Length(min = 5, max = 255, message = "Длина пароля должна быть не более 255 символов")
    @NotNull(message = "Пароль не может быть пустыми")
    private String password;

}
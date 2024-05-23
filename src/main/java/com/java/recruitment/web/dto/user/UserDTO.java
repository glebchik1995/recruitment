package com.java.recruitment.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.java.recruitment.web.dto.validation.OnCreate;
import com.java.recruitment.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User DTO")
public class UserDTO {

    @Schema(
            description = "User id", example = "1"
    )
    @NotNull(message = "ID не должен быть null.", groups = OnUpdate.class)
    private Long id;

    @Schema(
            description = "Email", example = "jondoe@gmail.com"
    )
    @Length(max = 255, message = "Email должен содержать от 5 до 255 символов.", groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(message = "Email не может быть пустыми.", groups = {OnCreate.class, OnUpdate.class})
    @Email(message = "Email адрес должен быть в формате user@example.com.")
    private String email;

    @Schema(
            description = "User email", example = "johndoe@gmail.com"
    )
    @NotNull(message = "Имя пользователя должно быть null.", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Длина имени пользователя должна быть меньше 255 символов.", groups = {OnCreate.class, OnUpdate.class})
    private String username;

    @Schema(
            description = "Зашифрованный пароль пользователя."
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Пароль должен быть null.", groups = {OnCreate.class, OnUpdate.class})
    private String password;

    @Schema(
            description = "Пароль подтверждение пользователя."
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password confirmation must be not null.", groups = {OnCreate.class})
    private String passwordConfirmation;

}

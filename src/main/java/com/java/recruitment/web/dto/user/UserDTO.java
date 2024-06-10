package com.java.recruitment.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.java.recruitment.service.model.user.Role;
import com.java.recruitment.web.dto.validation.FieldMatch;
import com.java.recruitment.web.dto.validation.OnCreate;
import com.java.recruitment.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "User DTO")
@FieldMatch
        (
                first = "password",
                second = "passwordConfirmation",
                message = "Поля пароля должны совпадать",
                groups = {OnCreate.class}
        )
public class UserDTO {

    @Schema(description = "User id", example = "1")
    @NotNull(message = "ID не должен быть null.", groups = OnUpdate.class)
    private Long id;

    @Schema(description = "User name", example = "John")
    @NotNull(message = "Имя должно быть не null.", groups = {OnCreate.class})
    @Length(
            max = 255, message = "Длина имени должна быть меньше 255 символов.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String name;

    @Schema(description = "User email", example = "johndoe@gmail.com")
    @Email(
            message = "Email адрес должен быть в формате user@example.com.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String username;

    @Schema(description = "Зашифрованный пароль пользователя.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Пароль не должен быть null.", groups = {OnCreate.class})
    private String password;

    @Schema(description = "Пароль подтверждение пользователя.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Подтверждение пароля не должно быть null.", groups = {OnCreate.class})
    @Null(groups = {OnUpdate.class})
    private String passwordConfirmation;

    @Schema(description = "Роли пользователя.")
    @Null(groups = {OnCreate.class})
    private Set<Role> roles;

}

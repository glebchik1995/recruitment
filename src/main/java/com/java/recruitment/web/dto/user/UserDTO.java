package com.java.recruitment.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.java.recruitment.service.model.user.Role;
import com.java.recruitment.validation.enums.EnumAllowedConstraint;
import com.java.recruitment.validation.match.FieldMatch;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@FieldMatch
        (
                first = "password",
                second = "passwordConfirmation",
                message = "Поля пароля должны совпадать"
        )
public class UserDTO {

    private Long id;

    @NotNull
    @Length(max = 255)
    private String name;

    @Schema(description = "User email", example = "johndoe@gmail.com")
    @Email
    private String username;

    @Schema(description = "Зашифрованный пароль пользователя.")
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Schema(description = "Пароль подтверждение пользователя.")
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordConfirmation;

    @NotNull
    @EnumAllowedConstraint(
            enumClass = Role.class,
            allowed =
                    {
                            "HR",
                            "INTERVIEW_SPECIALIST"
                    }

    )
    private Role role;

}

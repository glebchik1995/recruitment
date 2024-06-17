package com.java.recruitment.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.java.recruitment.service.model.user.Role;
import com.java.recruitment.validation.enums.EnumAllowedConstraint;
import com.java.recruitment.validation.match.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
@FieldMatch
        (
                first = "password",
                second = "passwordConfirmation",
                message = "Поле пароля и поле подтверждения пароля должны совпадать"
        )
public class UserDTO {

    private Long id;

    @NotBlank
    @Length(max = 255)
    private String name;

    @Email
    @NotBlank
    @Length(max = 255)
    private String username;

    @NotBlank
    @Length(max = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordConfirmation;

    @NotNull
    @EnumAllowedConstraint(
            enumClass = Role.class,
            allowed =
                    {
                            "HR",
                            "RECRUITER"
                    }

    )
    private Role role;

}

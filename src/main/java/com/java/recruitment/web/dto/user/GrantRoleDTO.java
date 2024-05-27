package com.java.recruitment.web.dto.user;

import com.java.recruitment.service.model.user.Role;
import com.java.recruitment.web.dto.validation.OnCreate;
import com.java.recruitment.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Grant Role DTO")
public class GrantRoleDTO {

    @Schema(description = "User id", example = "1")
    @NotNull(message = "ID не должен быть null.", groups = OnUpdate.class)
    private Long id;

    @Schema(description = "User role", example = "HR")
    @NotNull
    private Role role;
}

package com.java.recruitment.web.controller.admin;

import com.java.recruitment.service.IUserService;
import com.java.recruitment.web.dto.user.UserDTO;
import com.java.recruitment.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/user")
@RequiredArgsConstructor
@Validated
@Tag(name = "User Controller", description = "User API")
public class AdminController {

    private final IUserService userService;

    @PutMapping
    @Operation(summary = "Изменить данные пользователя")
    public UserDTO update(@Validated(OnUpdate.class) @RequestBody final UserDTO dto) {
        return userService.updateWithRoleAdmin(dto);
    }
}

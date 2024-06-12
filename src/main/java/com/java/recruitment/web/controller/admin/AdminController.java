package com.java.recruitment.web.controller.admin;

import com.java.recruitment.aspect.log.ToLog;
import com.java.recruitment.service.IUserService;
import com.java.recruitment.web.dto.user.UserDTO;
import com.java.recruitment.validation.marker.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(
        name = "Admin User Controller",
        description = "CRUD OPERATIONS WITH USERS"
)
@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@ToLog
public class AdminController {

    private final IUserService userService;

    @PutMapping
    @Operation(summary = "Изменить данные пользователя")
    public UserDTO update(@Validated(OnUpdate.class) @RequestBody final UserDTO dto) {
        return userService.updateWithRoleAdmin(dto);
    }

}

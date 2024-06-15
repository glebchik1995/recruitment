package com.java.recruitment.web.controller;

import com.java.recruitment.aspect.log.LogInfo;
import com.java.recruitment.service.IUserService;
import com.java.recruitment.web.dto.user.EditUserDTO;
import com.java.recruitment.web.dto.user.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "User Controller",
        description = "User API"
)
@RestController
@RequestMapping("/api/v1/users")
@Validated
@LogInfo
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PutMapping
    @Operation(summary = "Изменить данные пользователя")
    @PreAuthorize("@cse.canAccessUser(#dto.id)")
    public UserDTO update(@Valid @RequestBody final EditUserDTO dto) {
        return userService.editUser(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по идентификатору")
    @PreAuthorize("@cse.canAccessUser(#id)")
    public UserDTO getById(@PathVariable @Min(1) final Long id) {
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя по идентификатору")
    @PreAuthorize("@cse.canAccessUser(#id)")
    public void deleteById(@PathVariable @Min(1) final Long id) {
        userService.delete(id);
    }
}

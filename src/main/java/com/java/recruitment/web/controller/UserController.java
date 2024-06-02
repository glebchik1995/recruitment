package com.java.recruitment.web.controller;

import com.java.recruitment.service.IUserService;
import com.java.recruitment.web.dto.user.UserDTO;
import com.java.recruitment.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "User Controller", description = "User API")
public class UserController {

    private final IUserService userService;

    @PutMapping
    @Operation(summary = "Изменить данные пользователя")
    @PreAuthorize("@cse.canAccessUser(#dto.id)")
    public UserDTO update(@Validated(OnUpdate.class) @RequestBody final UserDTO dto) {
        return userService.update(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по идентификатору")
    @PreAuthorize("@cse.canAccessUser(#id)")
    public UserDTO getById(@PathVariable final Long id) {
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя по идентификатору")
    @PreAuthorize("@cse.canAccessUser(#id)")
    public void deleteById(@PathVariable final Long id) {
        userService.delete(id);
    }
}

package com.java.recruitment.web.controller;

import com.java.recruitment.aspect.log.ToLog;
import com.java.recruitment.service.IAuthService;
import com.java.recruitment.service.IUserService;
import com.java.recruitment.web.dto.auth.JwtRequest;
import com.java.recruitment.web.dto.auth.JwtResponse;
import com.java.recruitment.web.dto.user.UserDTO;
import com.java.recruitment.validation.marker.OnCreate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(
        name = "Auth Controller",
        description = "Auth API"
)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@ToLog
public class AuthController {

    private final IAuthService authService;
    private final IUserService userService;

    @PostMapping("/login")
    @Operation(summary = "Авторизоваться")
    public JwtResponse login(@Valid @RequestBody final JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/registration")
    @Operation(summary = "Зарегистрироваться")
    public UserDTO registration(@Validated(OnCreate.class) @RequestBody final UserDTO userDto) {
        return userService.create(userDto);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Обновить токен")
    public JwtResponse refresh(@RequestBody @NotBlank final String refreshToken) {
        return authService.refresh(refreshToken);
    }

}

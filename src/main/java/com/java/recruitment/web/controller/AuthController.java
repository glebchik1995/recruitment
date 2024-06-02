package com.java.recruitment.web.controller;

import com.java.recruitment.service.IAuthService;
import com.java.recruitment.service.IUserService;
import com.java.recruitment.web.dto.auth.JwtRequest;
import com.java.recruitment.web.dto.auth.JwtResponse;
import com.java.recruitment.web.dto.user.UserDTO;
import com.java.recruitment.web.dto.validation.OnCreate;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(
        name = "Auth Controller",
        description = "Auth API"
)
public class AuthController {

    private final IAuthService authService;
    private final IUserService userService;

    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody final JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/registration")
    public UserDTO registration(@Validated(OnCreate.class) @RequestBody final UserDTO userDto) {
        return userService.create(userDto);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody final String refreshToken) {
        return authService.refresh(refreshToken);
    }

}

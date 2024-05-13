package com.java.recruitment.service.impl;

import com.java.recruitment.repositoty.UserDAO;
import com.java.recruitment.repositoty.exception.DataAuthException;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.IAuthenticationService;
import com.java.recruitment.service.dto.JwtAuthenticationResponse;
import com.java.recruitment.service.dto.RefreshTokenRequest;
import com.java.recruitment.service.dto.SignInRequest;
import com.java.recruitment.service.dto.SignUpRequest;
import com.java.recruitment.service.model.Role;
import com.java.recruitment.service.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDAO userDAO;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    @Override
    @Transactional
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userService.create(user);

        String accessToken = jwtService.generateAccessToken(user);
        return new JwtAuthenticationResponse(accessToken);
    }


    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    @Override
    @Transactional
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            User user = userDAO.findByUsername(request.getUsername()).orElseThrow(()
                    -> new DataNotFoundException("Пользователь не найден"));
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            return new JwtAuthenticationResponse(accessToken, refreshToken);
        } catch (Exception e) {
            throw new DataAuthException("Аутентификация не удалась");
        }
    }

    /**
     * Обновление токена
     *
     * @param refreshTokenRequest данные пользователя
     * @return токен
     */
    @Override
    @Transactional
    public JwtAuthenticationResponse refresh(RefreshTokenRequest refreshTokenRequest) {
        String userName = jwtService.extractUserName(refreshTokenRequest.getRefreshToken());
        User user = userDAO.findByUsername(userName).orElseThrow(()
                -> new DataNotFoundException("Пользователь не найден"));

        if (jwtService.isTokenValid(refreshTokenRequest.getRefreshToken(), user)) {
            String newAccessToken = jwtService.generateAccessToken(user);
            return new JwtAuthenticationResponse(newAccessToken, refreshTokenRequest.getRefreshToken());
        } else {
            throw new DataAuthException("Невалидный токен JWT");
        }
    }
}
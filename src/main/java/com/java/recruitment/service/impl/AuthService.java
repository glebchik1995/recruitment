package com.java.recruitment.service.impl;

import com.java.recruitment.aspect.log.LogError;
import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.IAuthService;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.auth.JwtRequest;
import com.java.recruitment.web.dto.auth.JwtResponse;
import com.java.recruitment.web.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@LogError
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public JwtResponse login(final JwtRequest loginRequest) {
        JwtResponse jwtResponse = new JwtResponse();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword())
        );
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(()-> new DataNotFoundException("Пользователь не найден"));
        jwtResponse.setId(user.getId());
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(
                user.getId(), user.getUsername(), user.getRole())
        );
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(
                user.getId(), user.getUsername())
        );
        return jwtResponse;
    }

    @Override
    public JwtResponse refresh(final String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }

}

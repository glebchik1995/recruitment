package com.java.recruitment.service;

import com.java.recruitment.repositoty.UserDAO;
import com.java.recruitment.service.dto.JwtAuthenticationResponse;
import com.java.recruitment.service.dto.RefreshTokenRequest;
import com.java.recruitment.service.dto.SignInRequest;
import com.java.recruitment.service.dto.SignUpRequest;
import com.java.recruitment.service.impl.AuthenticationService;
import com.java.recruitment.service.impl.JwtService;
import com.java.recruitment.service.impl.UserService;
import com.java.recruitment.service.model.Role;
import com.java.recruitment.service.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void testSignUp() {
        SignUpRequest request = new SignUpRequest("username", "email", "password");
        User user = new User("username", "email", "password", Role.USER);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userService.create(any(User.class))).thenReturn(user);
        when(jwtService.generateAccessToken(any(User.class))).thenReturn("accessToken");

        JwtAuthenticationResponse response = authenticationService.signUp(request);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
    }

    @Test
    public void testSignIn() {
        SignInRequest request = new SignInRequest("username", "password");
        User user = new User("username", "email", "password", Role.USER);
        when(userDAO.findByUsername("username")).thenReturn(java.util.Optional.of(user));
        when(jwtService.generateAccessToken(user)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        JwtAuthenticationResponse response = authenticationService.signIn(request);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }

    @Test
    public void testRefresh() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("refreshToken");
        User user = new User("username", "email", "password", Role.USER);
        when(jwtService.extractUserName("refreshToken")).thenReturn("username");
        when(userDAO.findByUsername("username")).thenReturn(java.util.Optional.of(user));
        when(jwtService.isTokenValid("refreshToken", user)).thenReturn(true);
        when(jwtService.generateAccessToken(user)).thenReturn("newAccessToken");

        JwtAuthenticationResponse response = authenticationService.refresh(refreshTokenRequest);

        assertNotNull(response);
        assertEquals("newAccessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }
}

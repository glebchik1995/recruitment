package com.java.recruitment.service;

import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.impl.AuthService;
import com.java.recruitment.service.impl.UserService;
import com.java.recruitment.service.model.user.Role;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.auth.JwtRequest;
import com.java.recruitment.web.dto.auth.JwtResponse;
import com.java.recruitment.web.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static com.java.recruitment.service.model.user.Role.HR;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthService authenticationService;


    @Test
    void login() {
        Long userId = 1L;
        String username = "username";
        String password = "password";
        Role role = HR;
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setRole(role);
        Mockito.when(userService.getByUsername(username))
                .thenReturn(user);
        Mockito.when(tokenProvider.createAccessToken(userId, username, role))
                .thenReturn(accessToken);
        Mockito.when(tokenProvider.createRefreshToken(userId, username))
                .thenReturn(refreshToken);
        JwtResponse response = authenticationService.login(request);
        Mockito.verify(authenticationManager)
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword())
                );
        Assertions.assertEquals(response.getUsername(), username);
        Assertions.assertEquals(response.getId(), userId);
        Assertions.assertNotNull(response.getAccessToken());
        Assertions.assertNotNull(response.getRefreshToken());
    }

    @Test
    void loginWithIncorrectUsername() {
        String username = "username";
        String password = "password";
        JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);
        User user = new User();
        user.setUsername(username);
        Mockito.when(userService.getByUsername(username))
                .thenThrow(DataNotFoundException.class);
        Mockito.verifyNoInteractions(tokenProvider);
        Assertions.assertThrows(DataNotFoundException.class,
                () -> authenticationService.login(request));
    }

    @Test
    void refresh() {
        String refreshToken = "refreshToken";
        String accessToken = "accessToken";
        String newRefreshToken = "newRefreshToken";
        JwtResponse response = new JwtResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(newRefreshToken);
        Mockito.when(tokenProvider.refreshUserTokens(refreshToken))
                .thenReturn(response);
        JwtResponse testResponse = authenticationService.refresh(refreshToken);
        Mockito.verify(tokenProvider).refreshUserTokens(refreshToken);
        Assertions.assertEquals(testResponse, response);
    }

}

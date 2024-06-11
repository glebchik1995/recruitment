package com.java.recruitment.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.java.recruitment.BaseIntegrationTest;
import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.service.IAuthService;
import com.java.recruitment.service.model.user.Role;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.auth.JwtRequest;
import com.java.recruitment.web.dto.auth.JwtResponse;
import com.java.recruitment.web.mapper.UserMapper;
import com.java.recruitment.web.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = {"ADMIN"})
class AuthControllerTest extends BaseIntegrationTest {

    @Autowired(required = false)
    private MockMvc mvc;

    @Autowired
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IAuthService authService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("Вход в систему")
    void shouldLogin() throws Exception {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");
        Set<Role> someRoles = Set.of(Role.USER);

        JwtRequest jwtRequest = new JwtRequest("testUser", "password");
        JwtResponse expectedResponse = new JwtResponse();
        expectedResponse.setId(1L);
        expectedResponse.setUsername("testUser");
        expectedResponse.setAccessToken("mockAccessToken");
        expectedResponse.setRefreshToken("mockRefreshToken");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        when(jwtTokenProvider.createAccessToken(1L, "testUser", someRoles)).thenReturn("mockAccessToken");
        when(jwtTokenProvider.createRefreshToken(1L, "testUser")).thenReturn("mockRefreshToken");
        when(authService.login(jwtRequest)).thenReturn(expectedResponse);

        MockHttpServletResponse response = mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(jwtRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        JwtResponse actualResponse = mapper.readValue(response.getContentAsString(), JwtResponse.class);

        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
        assertEquals(expectedResponse.getAccessToken(), actualResponse.getAccessToken());
        assertEquals(expectedResponse.getRefreshToken(), actualResponse.getRefreshToken());
    }
}
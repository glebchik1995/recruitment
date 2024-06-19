package com.java.recruitment.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.java.recruitment.BaseIntegrationTest;
import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.service.IAuthService;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.auth.JwtRequest;
import com.java.recruitment.web.dto.auth.JwtResponse;
import com.java.recruitment.web.dto.user.UserDTO;
import com.java.recruitment.web.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
//@WithMockUser(username = "admin", authorities = {"ADMIN"})
class AuthControllerTest extends BaseIntegrationTest {

    @Autowired(required = false)
    private MockMvc mvc;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IAuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("Вход в систему")
    void shouldRegistrationAndLogin() throws Exception {

        UserDTO registerUserDTO = UserDTO.builder()
                .name("testName")
                .username("test@mail.com")
                .password("test123")
                .passwordConfirmation("test123")
                .build();

        MockHttpServletResponse responseRegistration = mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/registration")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(registerUserDTO))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        responseRegistration.setCharacterEncoding("UTF-8");

        User findUser = userRepository.findByUsername(registerUserDTO.getUsername()).orElse(null);
        Assertions.assertNotNull(findUser);

        findUser.setPassword(passwordEncoder.encode(findUser.getPassword()));
        UserDTO savedUser = userMapper.toDto(userRepository.save(findUser));

        Assertions.assertEquals(mapper.writeValueAsString(savedUser), responseRegistration.getContentAsString());

        JwtRequest jwtRequest = JwtRequest.builder()
                .username(savedUser.getUsername())
                .password("qwerty123")
                .build();

        MockHttpServletResponse responseLogin = mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(jwtRequest))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        responseLogin.setCharacterEncoding("UTF-8");

        JwtResponse actualResponse = authService.login(jwtRequest);

        Assertions.assertEquals(mapper.writeValueAsString(actualResponse), responseLogin.getContentAsString());

    }
}
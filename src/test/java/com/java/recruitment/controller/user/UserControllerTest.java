package com.java.recruitment.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.java.recruitment.BaseIntegrationTest;
import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.user.UpdateUserDTO;
import com.java.recruitment.web.dto.user.UserDTO;
import com.java.recruitment.web.mapper.UserMapper;
import com.java.recruitment.web.security.expression.CustomSecurityExpression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = "ADMIN")
class UserControllerTest extends BaseIntegrationTest {

    @Autowired(required = false)
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @MockBean
    private CustomSecurityExpression expression;


    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("Изменение публичных данных пользователя")
    void shouldUpdatePublicDataInOneUser() throws Exception {

        when(expression.canAccessUser(anyLong())).thenReturn(true);

        UpdateUserDTO updatedUserDTO = UpdateUserDTO.builder()
                .id(1L)
                .name("TEST_NAME")
                .username("test@gmail.com")
                .build();

        MockHttpServletResponse response = mvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updatedUserDTO))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        response.setCharacterEncoding("UTF-8");

        User candidate = userRepository.findById(updatedUserDTO.getId()).orElse(null);
        Assertions.assertNotNull(candidate);
        candidate.setName(updatedUserDTO.getName());
        candidate.setUsername(updatedUserDTO.getUsername());
        userRepository.save(candidate);

        UserDTO userDTO = userMapper.toDto(candidate);
        Assertions.assertEquals(mapper.writeValueAsString(userDTO), response.getContentAsString());
    }
}

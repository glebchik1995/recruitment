package com.java.recruitment.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.java.recruitment.BaseIntegrationTest;
import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.user.UserDTO;
import com.java.recruitment.web.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import static com.java.recruitment.service.model.user.Role.ADMIN;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = "ADMIN")
class AdminControllerTest extends BaseIntegrationTest {

    @Autowired(required = false)
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;


    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("Изменение любых данных пользователя")
    void shouldUpdateAnyDataInOneUser() throws Exception {

        UserDTO updatedUserDTO = UserDTO.builder()
                .id(1L)
                .name("TEST_NAME")
                .username("test@gmail.com")
                .password("$2a$12$x9Rhx2BKVeNd/iV0tpm1AOwFVhm835KvYILXSu5UHT19qMkbG3MSQ")
                .roles(Set.of(ADMIN))
                .build();

        MockHttpServletResponse response = mvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/admin/users"
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updatedUserDTO))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();


        response.setCharacterEncoding("UTF-8");

        User candidate = userRepository
                .findById(updatedUserDTO.getId())
                .orElse(null);

        Assertions.assertNotNull(candidate);

        candidate.setName(updatedUserDTO.getName());
        candidate.setUsername(updatedUserDTO.getUsername());
        candidate.setPassword(updatedUserDTO.getPassword());
        candidate.setRoles(updatedUserDTO.getRoles());


        userRepository.save(candidate);

        UserDTO userDTO = userMapper.toDto(candidate);

        Assertions.assertEquals(mapper.writeValueAsString(userDTO), response.getContentAsString());

    }
}
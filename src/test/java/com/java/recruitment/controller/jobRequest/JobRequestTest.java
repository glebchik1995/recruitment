package com.java.recruitment.controller.jobRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.java.recruitment.BaseIntegrationTest;
import com.java.recruitment.repositoty.JobRequestRepository;
import com.java.recruitment.service.model.hiring.JobRequest;
import com.java.recruitment.web.dto.hiring.JobResponseDTO;
import com.java.recruitment.web.mapper.impl.JobRequestMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = {"ADMIN"})
class JobRequestTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Autowired
    private JobRequestMapper jobRequestMapper;


    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("Изменение публичных данных пользователя")
    void shouldUpdatePublicDataInOneUser() throws Exception {

        MockHttpServletResponse response = mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/interviewer/job-request/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn().getResponse();


        response.setCharacterEncoding("UTF-8");
        JobRequest jobRequest = jobRequestRepository.findById(1L).orElse(null);
        Assertions.assertNotNull(jobRequest);

        JobResponseDTO jobRequestDTO = jobRequestMapper.toDto(jobRequest);
        Assertions.assertEquals(mapper.writeValueAsString(jobRequestDTO), response.getContentAsString());
    }
}

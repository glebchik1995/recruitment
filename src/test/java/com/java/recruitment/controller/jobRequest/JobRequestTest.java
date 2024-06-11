package com.java.recruitment.controller.jobRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.java.recruitment.BaseIntegrationTest;
import com.java.recruitment.repositoty.JobRequestRepository;
import com.java.recruitment.service.model.hiring.JobRequest;
import com.java.recruitment.web.dto.hiring.ChangeJobRequestStatusDTO;
import com.java.recruitment.web.dto.hiring.JobResponseDTO;
import com.java.recruitment.web.mapper.JobRequestMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static com.java.recruitment.service.model.hiring.Status.CANDIDATE_FOUND;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WithMockUser(username = "bigmax1993@mail.com", authorities = {"ADMIN"})
class JobRequestTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Autowired
    private JobRequestMapper jobRequestMapper;


    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("Получение заявки на работу по ID")
    void shouldGetJobRequestById() throws Exception {

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

    @Test
    @DisplayName("Изменение статуса заявки на работу по ID")
    void shouldUpdateJobRequestById() throws Exception {

        ChangeJobRequestStatusDTO jobRequestDto = ChangeJobRequestStatusDTO.builder()
                .id(1L)
                .status(CANDIDATE_FOUND)
                .build();

        MockHttpServletResponse response = mvc.perform(
                        MockMvcRequestBuilders.put
                                        (
                                                "/api/v1/interviewer/job-request/{id}",
                                                jobRequestDto.getId()
                                        )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(jobRequestDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");
        JobRequest jobRequest = jobRequestRepository.findById(jobRequestDto.getId()).orElse(null);
        Assertions.assertNotNull(jobRequest);

        jobRequest.setStatus(jobRequestDto.getStatus());
        jobRequestRepository.save(jobRequest);

        JobResponseDTO jobRequestDTO = jobRequestMapper.toDto(jobRequest);
        Assertions.assertEquals(mapper.writeValueAsString(jobRequestDTO), response.getContentAsString());
    }

    @Test
    @DisplayName("Удаление заявки на работу по ID")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteJobRequestById() throws Exception {
        Long jobRequestID = 1L;
        mvc.perform(MockMvcRequestBuilders.delete(
                                "/api/v1/interviewer/job-request/{id}",
                                jobRequestID
                        )
                )
                .andExpect(status().isOk());

        JobRequest jobRequest = jobRequestRepository.findById(jobRequestID).orElse(null);
        Assertions.assertNull(jobRequest);

    }

}

package com.java.recruitment.controller.interviewer;

import com.java.recruitment.BaseIntegrationTest;
import com.java.recruitment.repositoty.JobRequestRepository;
import com.java.recruitment.service.impl.FileService;
import com.java.recruitment.service.model.hiring.JobRequest;
import com.java.recruitment.web.mapper.JobRequestMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = {"ADMIN"})
public class InterviewerFileControllerTest extends BaseIntegrationTest {

    @Autowired(required = false)
    private MockMvc mvc;

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Autowired
    private JobRequestMapper jobRequestMapper;

    @Autowired
    private FileService fileService;

    @Test
    @DisplayName("Получение заявки на работу по ID")
    void shouldGetJobRequestById() throws Exception {

        Long jobRequestID = 1L;

        MockHttpServletResponse response = mvc.perform(
                        MockMvcRequestBuilders.get(
                                "/api/v1/interviewer/files/{id}",
                                jobRequestID
                        )
                )
                .andExpect(status().isOk())
                .andReturn().getResponse();


        response.setCharacterEncoding("UTF-8");
        JobRequest jobRequest = jobRequestRepository.findById(jobRequestID).orElse(null);
        Assertions.assertNotNull(jobRequest);
        String files = fileService.download(jobRequest.getId());
        assertEquals(files, response.getContentAsString());
    }
}

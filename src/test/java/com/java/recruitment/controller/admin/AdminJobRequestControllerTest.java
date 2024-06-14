package com.java.recruitment.controller.admin;

import com.java.recruitment.BaseIntegrationTest;
import com.java.recruitment.repositoty.JobRequestRepository;
import com.java.recruitment.service.model.jobRequest.JobRequest;
import com.java.recruitment.web.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = "ADMIN")
public class AdminJobRequestControllerTest extends BaseIntegrationTest {

    @Autowired(required = false)
    private MockMvc mvc;

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("Удаление заявки на работу по ID")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteJobRequestById() throws Exception {

        Long jobRequestID = 1L;

        mvc.perform(MockMvcRequestBuilders.delete(
                                "/api/v1/admin/job-request/{id}",
                                jobRequestID
                        )
                )
                .andExpect(status().isNoContent());

        JobRequest jobRequest = jobRequestRepository.findById(jobRequestID).orElse(null);
        Assertions.assertNull(jobRequest);

    }
}
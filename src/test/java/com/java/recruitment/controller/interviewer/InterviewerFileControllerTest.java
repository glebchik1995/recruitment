package com.java.recruitment.controller.interviewer;

import com.java.recruitment.BaseIntegrationTest;
import com.java.recruitment.repositoty.JobRequestRepository;
import com.java.recruitment.service.impl.FileService;
import com.java.recruitment.web.mapper.JobRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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

//    @Test
//    @DisplayName("Получение заявки на работу по ID")
//    void shouldGetJobRequestById() throws Exception {
//
//        Long jobRequestID = 1L;
//
//        MockHttpServletResponse response = mvc.perform(
//                        MockMvcRequestBuilders.get(
//                                "/api/v1/interviewer/files/{id}",
//                                jobRequestID
//                        )
//                )
//                .andExpect(status().isOk())
//                .andReturn().getResponse();
//
//
//        response.setCharacterEncoding("UTF-8");
//        JobRequest jobRequest = jobRequestRepository.findById(jobRequestID).orElse(null);
//        Assertions.assertNotNull(jobRequest);
//        String files = fileService.download(currentUser.getId(), jobRequest.getId());
//        assertEquals(files, response.getContentAsString());
//    }
}

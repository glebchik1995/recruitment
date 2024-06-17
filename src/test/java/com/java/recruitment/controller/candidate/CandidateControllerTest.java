package com.java.recruitment.controller.candidate;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.java.recruitment.BaseIntegrationTest;
import com.java.recruitment.repositoty.CandidateRepository;
import com.java.recruitment.service.model.candidate.Candidate;
import com.java.recruitment.web.dto.candidate.RequestCandidateDTO;
import com.java.recruitment.web.dto.candidate.ResponseCandidateDTO;
import com.java.recruitment.web.mapper.CandidateMapper;
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
import com.java.recruitment.util.NullPropertyCopyHelper;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = {"ADMIN"})
class CandidateControllerTest extends BaseIntegrationTest {

    @Autowired(required = false)
    private MockMvc mvc;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CandidateMapper candidateMapper;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("Редактирование всех полей у одного кандидата")
    void shouldEditingAllDataOneCandidate() throws Exception {

        RequestCandidateDTO updatedCandidateDTO = RequestCandidateDTO.builder()
                .id(1L)
                .age(55)
                .name("TEST_NAME")
                .surname("TEST_SURNAME")
                .phone("+7 (999) 999-99-99")
                .email("test@mail.com")
                .position("TEST POSITION")
                .build();

        MockHttpServletResponse response = mvc.perform(
                        MockMvcRequestBuilders.put(
                                        "/api/v1/hr/candidate"
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updatedCandidateDTO))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");

        Candidate candidate = candidateRepository
                .findById(updatedCandidateDTO.getId())
                .orElse(null);

        Assertions.assertNotNull(candidate);

        candidate.setAge(updatedCandidateDTO.getAge());
        candidate.setName(updatedCandidateDTO.getName());
        candidate.setSurname(updatedCandidateDTO.getSurname());
        candidate.setEmail(updatedCandidateDTO.getEmail());
        candidate.setPhone(updatedCandidateDTO.getPhone());
        candidate.setPosition(updatedCandidateDTO.getPosition());

        candidateRepository.save(candidate);

        ResponseCandidateDTO candidateDTO = candidateMapper.toDto(candidate);

        Assertions.assertEquals(mapper.writeValueAsString(candidateDTO), response.getContentAsString());

    }

    @Test
    @DisplayName("Редактирование всех полей у одного кандидата")
    void shouldEditingDataOneCandidateExceptFieldsEqualsToNull() throws Exception {

        RequestCandidateDTO updatedCandidateDTO = RequestCandidateDTO.builder()
                .id(1L)
                .age(55)
                .name(null)
                .surname(null)
                .phone(null)
                .email(null)
                .position(null)
                .build();

        MockHttpServletResponse response = mvc.perform(
                        MockMvcRequestBuilders.put(
                                        "/api/v1/hr/candidate"
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updatedCandidateDTO))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");

        Candidate candidate = candidateRepository
                .findById(updatedCandidateDTO.getId())
                .orElse(null);

        Assertions.assertNotNull(candidate);

        NullPropertyCopyHelper.copyNonNullProperties(updatedCandidateDTO, candidate);

        candidateRepository.save(candidate);

        ResponseCandidateDTO candidateDTO = candidateMapper.toDto(candidate);

        Assertions.assertNotNull(candidateDTO.getSurname());
        Assertions.assertNotNull(candidateDTO.getEmail());
        Assertions.assertNotNull(candidateDTO.getPhone());
        Assertions.assertNotNull(candidateDTO.getPosition());

        Assertions.assertEquals(mapper.writeValueAsString(candidateDTO), response.getContentAsString());

    }

}

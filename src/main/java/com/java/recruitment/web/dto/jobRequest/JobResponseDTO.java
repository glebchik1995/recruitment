package com.java.recruitment.web.dto.jobRequest;

import com.java.recruitment.service.model.jobRequest.Status;
import com.java.recruitment.web.dto.candidate.RequestCandidateDTO;
import com.java.recruitment.web.dto.user.UserDTO;
import com.java.recruitment.web.dto.vacancy.ResponseVacancyDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class JobResponseDTO {

    private Long id;

    private Status status;

    private UserDTO hr;

    private RequestCandidateDTO candidate;

    private ResponseVacancyDTO vacancyDTO;

    private List<String> files;

    private String description;
}

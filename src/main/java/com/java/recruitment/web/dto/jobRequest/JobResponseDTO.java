package com.java.recruitment.web.dto.jobRequest;

import com.java.recruitment.service.model.jobRequest.Status;
import com.java.recruitment.web.dto.candidate.CandidateDTO;
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

    private CandidateDTO candidate;

    private ResponseVacancyDTO recruiter;

    private List<String> files;

    private String description;
}

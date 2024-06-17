package com.java.recruitment.web.dto.vacancy;

import com.java.recruitment.web.dto.user.UserDTO;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ResponseVacancyDTO {

    private Long id;

    private String requirement;

    private String description;

    private LocalTime startWorkingDay;

    private String endWorkingDay;

    private int salary;

    private LocalDate createdDate;

    private LocalTime createdTime;

    private boolean active;

    private UserDTO recruiter;
}

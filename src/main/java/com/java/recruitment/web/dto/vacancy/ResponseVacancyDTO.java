package com.java.recruitment.web.dto.vacancy;

import com.java.recruitment.service.model.user.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private String salary;

    private LocalDate createdDate;

    private LocalDateTime createdTime;

    private boolean active;

    private User interviewSpecialist;
}

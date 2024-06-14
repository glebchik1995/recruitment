package com.java.recruitment.web.dto.vacancy;

import com.java.recruitment.validation.marker.OnCreate;
import com.java.recruitment.validation.marker.OnUpdate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RequestVacancyDTO {

    @Min(value = 1, groups = {OnUpdate.class})
    private Long id;

    @NotNull(groups = {OnCreate.class})
    private String requirement;

    @NotNull(groups = {OnCreate.class})
    private String description;

    @NotNull(groups = {OnCreate.class})
    private LocalTime startWorkingDay;

    @NotNull(groups = {OnCreate.class})
    private LocalTime endWorkingDay;

    @NotNull(groups = {OnCreate.class})
    private String salary;

    @NotNull(groups = {OnCreate.class})
    private boolean active;
}

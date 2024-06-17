package com.java.recruitment.web.dto.vacancy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.java.recruitment.validation.marker.OnCreate;
import com.java.recruitment.validation.marker.OnUpdate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class RequestVacancyDTO {

    @Min(value = 1, groups = {OnUpdate.class})
    private Long id;

    @NotBlank(groups = {OnCreate.class})
    private String requirement;

    @NotBlank(groups = {OnCreate.class})
    @Length(max = 255)
    private String title;

    @NotBlank(groups = {OnCreate.class})
    @Length(max = 255)
    private String position;

    @NotBlank(groups = {OnCreate.class})
    private String description;

    @NotNull(groups = {OnCreate.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startWorkingDay;

    @NotNull(groups = {OnCreate.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime endWorkingDay;

    @NotNull(groups = {OnCreate.class})
    private int salary;

}

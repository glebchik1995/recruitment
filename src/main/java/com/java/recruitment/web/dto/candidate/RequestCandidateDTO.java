package com.java.recruitment.web.dto.candidate;

import com.java.recruitment.validator.marker.OnCreate;
import com.java.recruitment.validator.marker.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class RequestCandidateDTO {

    @Min(groups = OnUpdate.class, value = 1)
    private Long id;

    @NotBlank(groups = {OnCreate.class})
    @Length(max = 255, groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotBlank(groups = {OnCreate.class})
    @Length(max = 255, groups = {OnCreate.class, OnUpdate.class})
    private String surname;

    @Min(groups = {OnCreate.class, OnUpdate.class}, value = 18, message = "Возраст кандидата должен быть 18+")
    @Max(groups = {OnCreate.class, OnUpdate.class}, value = 80, message = "Возраст кандидата должен меньше 80")
    private int age;

    @Email(groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, groups = {OnCreate.class, OnUpdate.class})
    private String email;

    @Pattern(groups = {OnCreate.class, OnUpdate.class}, regexp = "^\\+7\\s\\(\\d{3}\\)\\s\\d{3}-\\d{2}-\\d{2}$")
    @NotBlank(groups = {OnCreate.class})
    @Schema(description = "phone", example = "+7 (XXX) XXX-XX-XX")
    @Length(max = 20, groups = {OnCreate.class, OnUpdate.class})
    private String phone;

    @NotBlank(groups = {OnCreate.class})
    @Length(max = 255, groups = {OnCreate.class, OnUpdate.class})
    private String position;

    @NotBlank(groups = {OnCreate.class})
    @Length(max = 1000, groups = {OnCreate.class, OnUpdate.class})
    private String exp;

    @NotBlank(groups = {OnCreate.class})
    @Length(max = 255, groups = {OnCreate.class, OnUpdate.class})
    private String techSkill;

    @NotBlank(groups = {OnCreate.class})
    @Length(max = 255, groups = {OnCreate.class, OnUpdate.class})
    private String languageSkill;

    @NotNull(groups = {OnCreate.class})
    private int expectedSalary;
}

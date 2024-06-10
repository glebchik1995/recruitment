package com.java.recruitment.web.dto.candidate;

import com.java.recruitment.web.dto.validation.OnCreate;
import com.java.recruitment.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Candidate DTO")
public class CandidateDTO {

    @NotNull(groups = OnUpdate.class)
    private Long id;

    @NotBlank(groups = {OnCreate.class})
    @Length(max = 20, groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotBlank(groups = {OnCreate.class})
    @Length(max = 20, groups = {OnCreate.class, OnUpdate.class})
    private String surname;

    @Min(groups = {OnCreate.class, OnUpdate.class}, value = 18)
    @Max(groups = {OnCreate.class, OnUpdate.class}, value = 80)
    @NotNull(groups = {OnCreate.class})
    private int age;

    @Email(groups = {OnCreate.class, OnUpdate.class})
    @Schema(description = "email", example = "ivan1977@mail.com")
    private String email;

    @Pattern(groups = {OnCreate.class, OnUpdate.class}, regexp = "^\\+7\\s\\(\\d{3}\\)\\s\\d{3}-\\d{2}-\\d{2}$")
    @NotBlank(groups = {OnCreate.class})
    @Schema(description = "phone", example = "+7 (XXX) XXX-XX-XX")
    private String phone;

    @NotBlank(groups = {OnCreate.class})
    private String position;

    @NotBlank(groups = {OnCreate.class})
    private String department;
}

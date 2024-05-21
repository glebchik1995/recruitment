package com.java.recruitment.service.dto.candidate;

import com.java.recruitment.service.dto.validationGroup.OnCreate;
import com.java.recruitment.service.dto.validationGroup.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidateDTO {

    private Long id;

    @NotBlank(groups = {OnCreate.class})
    @Schema(description = "name", example = "Иван")
    private String name;

    @NotBlank(groups = {OnCreate.class})
    @Schema(description = "surname", example = "Иванов")
    private String surname;

    @Min(groups = {OnCreate.class, OnUpdate.class}, value = 18)
    @Max(groups = {OnCreate.class, OnUpdate.class}, value = 80)
    @NotNull
    @Schema(description = "age", example = "22")
    private int age;

    @Email(groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(groups = {OnCreate.class})
    @Schema(description = "email", example = "ivan1977@mail.com")
    private String email;

    @Pattern(groups = {OnCreate.class, OnUpdate.class}, regexp = "^\\+7\\s\\(\\d{3}\\)\\s\\d{3}-\\d{2}-\\d{2}$")
    @NotBlank(groups = {OnCreate.class})
    @Schema(description = "phone", example = "+7 (XXX) XXX-XX-XX")
    private String phone;

    @NotBlank(groups = {OnCreate.class})
    @Schema(description = "position", example = "Middle Java Developer")
    private String position;

    @NotBlank(groups = {OnCreate.class})
    @Schema(description = "department", example = "IT")
    private String department;
}

package com.java.recruitment.web.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder(toBuilder = true)
public class EditUserDTO {

    @Min(1)
    private Long id;

    @Length(max = 255)
    private String name;

    @Email
    @Length(max = 255)
    private String username;
}

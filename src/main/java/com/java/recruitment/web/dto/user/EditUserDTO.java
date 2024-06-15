package com.java.recruitment.web.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class EditUserDTO {

    @Min(1)
    private Long id;

    private String name;

    @Email
    private String username;
}

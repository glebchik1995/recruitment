package com.java.recruitment.web.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
@Schema(description = "Short User DTO")
public class ShortUserDTO {

    @Schema(description = "User id", example = "1")
    @Min(1)
    private Long id;

    @Schema(description = "User name", example = "John")
    private String name;

    @Schema(description = "User email", example = "johndoe@gmail.com")
    @Email(message = "Email адрес должен быть в формате user@example.com.")
    private String username;
}

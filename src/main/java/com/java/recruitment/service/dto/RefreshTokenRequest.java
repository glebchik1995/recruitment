package com.java.recruitment.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Обновление токена")
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {

    @Schema(description = "Обновленный токен доступа", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImFkbWluIiwiaWF0IjoxNjIyNTA2...")
    @NotBlank(message = "Токен не может быть пустыми")
    private String refreshToken;
}

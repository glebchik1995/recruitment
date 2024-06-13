package com.java.recruitment.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "Ответ после входа в систему")
public class JwtResponse {

    private Long id;
    private String username;
    private String accessToken;
    private String refreshToken;

}

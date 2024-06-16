package com.java.recruitment.web.dto.chat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChatMessageRequestDTO {

    @Min(1)
    private Long recipientId;

    @NotBlank
    private String text;
}

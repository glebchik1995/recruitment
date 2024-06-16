package com.java.recruitment.web.dto.chat;

import com.java.recruitment.web.dto.user.UserDTO;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChatMessageResponseDTO {

    private Long id;

    private UserDTO sender;

    private UserDTO recipient;

    private String text;

    private LocalDate sentDate;

    private LocalTime sentTime;
}

package com.java.recruitment.web.dto.chat;

import com.java.recruitment.service.model.chat.Sender;
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

    private UserDTO hr;

    private UserDTO recruiter;

    private Sender sender;

    private String text;

    private LocalDate sentDate;

    private LocalTime sentTime;
}

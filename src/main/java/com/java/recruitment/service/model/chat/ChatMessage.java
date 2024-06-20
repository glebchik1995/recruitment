package com.java.recruitment.service.model.chat;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.java.recruitment.service.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "chat_message")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hr_id", referencedColumnName = "id", nullable = false)
    @JsonManagedReference
    private User hr;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recruiter_id", referencedColumnName = "id", nullable = false)
    @JsonManagedReference
    private User recruiter;

    @Column(name = "sender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Sender sender;

    @Column(name = "text")
    private String text;

    @Column(name = "sent_date")
    private LocalDate sentDate;

    @Column(name = "sent_time")
    private LocalTime sentTime;

}

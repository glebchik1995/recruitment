package com.java.recruitment.service.model.message;

import jakarta.persistence.*;
import lombok.*;


import java.util.Date;

@Entity
@Table(name = "chat_message")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "sender_id")
    private String senderId;

    @Column(name = "recipient_id")
    private String recipientId;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "recipient_name")
    private String recipientName;

    @Column(name = "content")
    private String content;

    @Column(name = "timestamp")
    private Date timestamp;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MessageStatus status;
}
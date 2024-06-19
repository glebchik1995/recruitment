package com.java.recruitment.service.model.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.java.recruitment.service.model.candidate.Candidate;
import com.java.recruitment.service.model.chat.ChatMessage;
import com.java.recruitment.service.model.jobRequest.JobRequest;
import com.java.recruitment.service.model.vacancy.Vacancy;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Transient
    private String passwordConfirmation;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "hr", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonBackReference
    private List<JobRequest> jobRequestsByHr;

    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonBackReference
    private List<JobRequest> jobRequestsByRecruiter;

    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonBackReference
    private List<Vacancy> vacancies;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonBackReference
    private List<ChatMessage> sentMessages;

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonBackReference
    private List<ChatMessage> receivedMessages;

    @OneToMany(mappedBy = "hr", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonBackReference
    private List<Candidate> candidates;
}

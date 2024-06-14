package com.java.recruitment.service.model.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<JobRequest> jobRequests;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Vacancy> vacancies;
}

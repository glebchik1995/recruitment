package com.java.recruitment.service.model.jobRequest;

import com.java.recruitment.service.model.candidate.Candidate;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.service.model.vacancy.Vacancy;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "job_request")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
public class JobRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hr_id", referencedColumnName = "id", nullable = false)
    private User hr;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "candidate_id", referencedColumnName = "id", nullable = false)
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vacancy_id", referencedColumnName = "id", nullable = false)
    private Vacancy vacancy;

    @Column(name = "file")
    @CollectionTable(name = "job_request_files")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> files;

    private String description;
}
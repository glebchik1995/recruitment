package com.java.recruitment.service.model.hiring;

import com.java.recruitment.service.model.hr.HR;
import com.java.recruitment.service.model.candidate.Candidate;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "job_requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class JobRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "hr_id", nullable = false)
    private HR hr;

    @OneToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Column(name = "files")
    @CollectionTable(name = "job_request_files")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> files;

    @Column(name = "description")
    private String description;
}

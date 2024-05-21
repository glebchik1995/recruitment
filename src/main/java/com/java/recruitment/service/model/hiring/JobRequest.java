package com.java.recruitment.service.model.hiring;

import com.java.recruitment.service.model.hr.HR;
import com.java.recruitment.service.model.candidate.Candidate;
import com.java.recruitment.service.model.attachment.AttachedFile;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "job_requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class JobRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_request_status", nullable = false)
    private JobRequestStatus status;

    @ManyToOne
    @JoinColumn(name = "hr_id", nullable = false)
    private HR hr;

    @OneToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @OneToMany(mappedBy = "job_requests", cascade = CascadeType.ALL)
    private List<AttachedFile> files;

    @Column(name = "description")
    private String description;
}

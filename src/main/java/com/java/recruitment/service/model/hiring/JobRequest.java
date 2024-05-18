package com.java.recruitment.service.model.hiring;

import com.java.recruitment.service.model.candidate.Candidate;
import com.java.recruitment.service.model.user.User;
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

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String department;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobRequestStatus status;

    @ManyToOne
    @JoinColumn(name = "hr_id", nullable = false)
    private User hr;

    @OneToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @OneToMany(mappedBy = "hiringRequest", cascade = CascadeType.ALL)
    private List<AttachedFile> files;
}

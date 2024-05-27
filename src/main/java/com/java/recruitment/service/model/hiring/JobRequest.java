package com.java.recruitment.service.model.hiring;

import com.java.recruitment.service.model.candidate.Candidate;
import com.java.recruitment.service.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "job_request")
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
    @JoinColumn(name = "hr_id", referencedColumnName = "id", nullable = false)
    private User hr;

    @OneToOne
    @JoinColumn(name = "candidate_id", referencedColumnName = "id", nullable = false)
    private Candidate candidate;

//    FetchType.EAGER означает, что связанные сущности будут загружены немедленно вместе с основной сущностью.
//    Это означает, что при запросе основной сущности JPA также автоматически будет загружать все связанные сущности.
//    Это может привести к избыточной загрузке данных, особенно если связанные данные не нужны в данном контексте.
    @Column(name = "file")
    @CollectionTable(name = "job_request_files")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> files;

    @Column(name = "description")
    private String description;
}

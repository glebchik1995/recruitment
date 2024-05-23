package com.java.recruitment.service.model.hiring;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_requset_file")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class JobRequestFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "size", nullable = false)
    private long size;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @ManyToOne
    @JoinColumn(name = "job_request_id")
    private JobRequest jobRequest;
}

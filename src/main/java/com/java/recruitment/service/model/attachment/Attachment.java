package com.java.recruitment.service.model.attachment;

import com.java.recruitment.service.model.hiring.HiringRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attachments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "hiring_request_id")
    private HiringRequest hiringRequest;

}

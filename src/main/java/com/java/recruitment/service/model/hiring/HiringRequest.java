package com.java.recruitment.service.model.hiring;

import com.java.recruitment.service.model.user.User;
import com.java.recruitment.service.model.attachment.Attachment;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "hiring_requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class HiringRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String department;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HiringRequestStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "hiringRequest", cascade = CascadeType.ALL)
    private List<Attachment> attachments;
}

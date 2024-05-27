package com.java.recruitment.service.model.candidate;

import com.java.recruitment.service.model.hiring.JobRequest;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "candidate")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Candidate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    FetchType.LAZY означает, что связанные сущности будут загружены только по требованию.
//    То есть, данные будут загружены только в тот момент, когда к ним будет обращение.
//    Это помогает оптимизировать загрузку данных и уменьшить объем передаваемой информации.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_request_id", referencedColumnName = "id", nullable = false)
    private JobRequest jobRequest;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "department", nullable = false)
    private String department;

}

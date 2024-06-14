package com.java.recruitment.service.model.candidate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.java.recruitment.service.model.jobRequest.JobRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "candidate")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(nullable = false)
    private String exp;

    @Column(name = "tech_skill", nullable = false)
    private String techSkill;

    @Column(name = "language_skill", nullable = false)
    private String languageSkill;

    @Column(name = "expected_salary", nullable = false)
    private String expectedSalary;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<JobRequest> jobRequests;

}

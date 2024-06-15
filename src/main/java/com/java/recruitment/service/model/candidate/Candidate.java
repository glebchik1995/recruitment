package com.java.recruitment.service.model.candidate;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String exp;

    @Column(name = "tech_skill", nullable = false)
    private String techSkill;

    @Column(name = "language_skill", nullable = false)
    private String languageSkill;

    @Column(name = "expected_salary", nullable = false)
    private int expectedSalary;

}

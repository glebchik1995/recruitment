package com.java.recruitment.service.model.vacancy;

import com.java.recruitment.service.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "vacancy")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String requirement;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String description;

    @Column(name = "start_working_day", nullable = false)
    private LocalTime startWorkingDay;

    @Column(name = "end_working_day", nullable = false)
    private LocalTime endWorkingDay;

    @Column(nullable = false)
    private int salary;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @Column(name = "created_time", nullable = false)
    private LocalTime createdTime;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recruiter_id", referencedColumnName = "id", nullable = false)
    private User recruiter;

}
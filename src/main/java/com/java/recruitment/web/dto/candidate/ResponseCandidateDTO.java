package com.java.recruitment.web.dto.candidate;

import com.java.recruitment.web.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseCandidateDTO {

    private Long id;

    private String name;

    private String surname;

    private int age;

    private String email;

    private String phone;

    private String position;

    private String exp;

    private String techSkill;

    private String languageSkill;

    private int expectedSalary;

    private UserDTO hr;
}

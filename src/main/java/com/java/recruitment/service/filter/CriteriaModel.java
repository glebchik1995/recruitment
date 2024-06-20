package com.java.recruitment.service.filter;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CriteriaModel implements Serializable {

    private String field;

    private Operation operation;

    private Object value;
}

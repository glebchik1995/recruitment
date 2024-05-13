package com.java.recruitment.service.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CriteriaModel {

    private String field;

    private Operation operation;

    private Object value;
}

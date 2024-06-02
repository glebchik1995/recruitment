package com.java.recruitment.service.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriteriaModel {

    private String field;

    private Operation operation;

    private Object value;

    private JoinType joinType;
}

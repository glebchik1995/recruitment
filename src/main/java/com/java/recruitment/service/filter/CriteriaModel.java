package com.java.recruitment.service.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CriteriaModel implements Serializable {

    private String field;

    private Operation operation;

    private Object value;

    private JoinType joinType;

    public CriteriaModel(String field, Operation operation, Object value) {
        this.field = field;
        this.operation = operation;
        this.value = value;
    }
}

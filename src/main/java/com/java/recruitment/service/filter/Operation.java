package com.java.recruitment.service.filter;

import java.io.Serializable;

public enum Operation implements Serializable {
    /**
     * IS_NULL - не равно null
     * IS_NOT_NULL - не равно null
     * EQUALS - равно
     * NOT_EQUALS - не равно
     * LIKE - похоже на
     * NOT_LIKE - не похоже на
     * GREATER_THAN - больше чем
     * GREATER_THAN_OR_EQUALS - больше или равно
     * LESS_THAN - меньше чем
     * LESS_THAN_OR_EQUALS - меньше или равно
     * IN - входит в
     * NOT_IN - не входит в
     */

    IS_NULL,
    IS_NOT_NULL,
    EQUALS,
    NOT_EQUALS,
    LIKE,
    NOT_LIKE,
    GREATER_THAN,
    GREATER_THAN_OR_EQUALS,
    LESS_THAN,
    LESS_THAN_OR_EQUALS,
    IN,
    NOT_IN
}

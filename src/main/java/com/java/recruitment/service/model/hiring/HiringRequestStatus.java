package com.java.recruitment.service.model.hiring;

import java.io.Serializable;

public enum HiringRequestStatus implements Serializable {

    NEW("NEW"),
    CANDIDATE_FOUND("CANDIDATE_FOUND"),
    INTERVIEW("INTERVIEW"),
    HIRED("HIRED"),
    REJECTED("REJECTED");

    private final String value;

    HiringRequestStatus(String value) {
        this.value = value;
    }

    public static HiringRequestStatus fromValue(String value) {
        for (HiringRequestStatus status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Недопустимое значение для статуса: " + value);
    }
}

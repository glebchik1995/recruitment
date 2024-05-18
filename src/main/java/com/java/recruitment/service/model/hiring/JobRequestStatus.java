package com.java.recruitment.service.model.hiring;

import java.io.Serializable;

public enum JobRequestStatus implements Serializable {

    NEW("NEW"),
    CANDIDATE_FOUND("CANDIDATE_FOUND"),
    INTERVIEW("INTERVIEW"),
    HIRED("HIRED"),
    REJECTED("REJECTED");

    private final String value;

    JobRequestStatus(String value) {
        this.value = value;
    }

    public static JobRequestStatus fromValue(String value) {
        for (JobRequestStatus status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Недопустимое значение для статуса: " + value);
    }
}

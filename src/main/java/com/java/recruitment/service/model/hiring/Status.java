package com.java.recruitment.service.model.hiring;

import lombok.Getter;

@Getter
public enum Status {

    NEW("NEW"),
    CANDIDATE_FOUND("CANDIDATE_FOUND"),
    INTERVIEW("INTERVIEW"),
    HIRED("HIRED"),
    REJECTED("REJECTED");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public static Status fromValue(Status value) {
        for (Status status : values()) {
            if (status.value.equalsIgnoreCase(value.getValue())) {
                return status;
            }
        }
        throw new IllegalArgumentException("Недопустимое значение для StatusRoom: " + value);
    }
}


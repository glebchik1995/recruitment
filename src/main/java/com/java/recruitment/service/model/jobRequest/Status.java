package com.java.recruitment.service.model.jobRequest;

import lombok.Getter;

@Getter
public enum Status {

    NEW,
    CANDIDATE_FOUND,
    INTERVIEW,
    HIRED,
    REJECTED
}


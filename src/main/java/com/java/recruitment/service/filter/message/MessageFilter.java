package com.java.recruitment.service.filter.message;

import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.model.user.User;

import java.time.LocalDateTime;

public class MessageFilter {

    private CriteriaModel criteriaModel;

    private String text;

    private User sender;

    private User receiver;

    private LocalDateTime createdAt;
}

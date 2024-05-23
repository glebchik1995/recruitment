package com.java.recruitment.service.model.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    HR("HR"),
    INTERVIEW_SPECIALIST("INTERVIEW_SPECIALIST"),
    ADMIN("ADMIN");

    private final String vale;

    @Override
    public String getAuthority() {
        return vale;
    }

}

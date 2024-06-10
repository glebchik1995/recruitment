package com.java.recruitment.web.security.expression;

import com.java.recruitment.service.IUserService;
import com.java.recruitment.service.model.user.Role;
import com.java.recruitment.web.security.JwtEntity;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("cse")
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final IUserService userService;

    public boolean canAccessUser(@NotNull final Long id) {
        JwtEntity user = this.getPrincipal();
        Long userId = user.getId();
        return userId.equals(id) || hasAnyRole(Role.ADMIN);
    }

    public boolean canAccessInterviewer(final Long id) {
        JwtEntity user = this.getPrincipal();
        Long userId = user.getId();

        return (userId.equals(id) && hasAnyRole(Role.INTERVIEW_SPECIALIST)) || hasAnyRole(Role.ADMIN);
    }

    public boolean canAccessHr(final Long id) {
        JwtEntity user = this.getPrincipal();
        Long userId = user.getId();

        return (userId.equals(id) && hasAnyRole(Role.HR)) || hasAnyRole(Role.ADMIN);
    }

    public boolean canAccessJobRequest(final Long jobRequestId) {
        JwtEntity user = this.getPrincipal();
        Long id = user.getId();

        return userService.isJobRequestOwner(id, jobRequestId);
    }

    private boolean hasAnyRole(final Role... roles) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        for (Role role : roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }

    private JwtEntity getPrincipal() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        return (JwtEntity) authentication.getPrincipal();
    }
}

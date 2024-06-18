package com.java.recruitment.web.security.expression;

import com.java.recruitment.repositoty.exception.DataAccessException;
import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.service.model.user.Role;
import com.java.recruitment.web.security.JwtEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("cse")
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final IJobRequestService jobRequestService;

    public boolean canAccessUser(final Long id) {
        JwtEntity user = this.getPrincipal();
        Long userId = user.getId();
        return userId.equals(id) || hasAnyRole(Role.ADMIN);
    }

    public Long getIdFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtEntity) {
            return ((JwtEntity) authentication.getPrincipal()).getId();
        }
        throw new DataAccessException("Пользователь не аутентифицирован или неверные данные аутентификации");
    }

    public boolean isJobRequestOwner(final Long jobRequestId) {
        JwtEntity user = this.getPrincipal();
        Long id = user.getId();

        return jobRequestService.isJobRequestOwner(id, jobRequestId) || hasAnyRole(Role.ADMIN);
    }

    public boolean isJobRequestConsumer(final Long jobRequestId) {
        JwtEntity user = this.getPrincipal();
        Long id = user.getId();

        return jobRequestService.isJobRequestConsumer(id, jobRequestId) || hasAnyRole(Role.ADMIN);
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

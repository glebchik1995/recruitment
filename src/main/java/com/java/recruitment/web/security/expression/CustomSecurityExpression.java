package com.java.recruitment.web.security.expression;

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

    public boolean canAccessUser(final Long id) {
        JwtEntity user = this.getPrincipal();
        Long userId = user.getId();
        return userId.equals(id) || hasAnyRole(Role.ADMIN);
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

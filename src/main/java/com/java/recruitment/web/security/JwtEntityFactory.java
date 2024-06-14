package com.java.recruitment.web.security;

import com.java.recruitment.service.model.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

public final class JwtEntityFactory {
    public static JwtEntity create(final User user) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
        List<GrantedAuthority> authorities = Collections.singletonList(authority);

        return new JwtEntity(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}

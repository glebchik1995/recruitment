package com.java.recruitment.web.security;

import com.java.recruitment.service.filter.UserService;
import com.java.recruitment.service.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        User user = userService.getByUsername(username);
        return JwtEntityFactory.create(user);
    }

}

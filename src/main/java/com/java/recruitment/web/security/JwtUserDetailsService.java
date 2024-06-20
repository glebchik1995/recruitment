package com.java.recruitment.web.security;

import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new DataNotFoundException("Пользователь не найден"));
        return JwtEntityFactory.create(user);
    }

}

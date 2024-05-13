package com.java.recruitment.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtService {

    String extractUserName(String token);

    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);
}

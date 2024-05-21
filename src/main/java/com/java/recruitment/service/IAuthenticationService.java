package com.java.recruitment.service;

import com.java.recruitment.service.dto.auth.JwtAuthenticationResponse;
import com.java.recruitment.service.dto.auth.RefreshTokenRequest;
import com.java.recruitment.service.dto.auth.SignInRequest;
import com.java.recruitment.service.dto.auth.SignUpRequest;

public interface IAuthenticationService {

    JwtAuthenticationResponse signUp(SignUpRequest request);

    JwtAuthenticationResponse signIn(SignInRequest request);

    JwtAuthenticationResponse refresh(RefreshTokenRequest refreshTokenRequest);
}

package com.java.recruitment.service;

import com.java.recruitment.service.dto.JwtAuthenticationResponse;
import com.java.recruitment.service.dto.RefreshTokenRequest;
import com.java.recruitment.service.dto.SignInRequest;
import com.java.recruitment.service.dto.SignUpRequest;

public interface IAuthenticationService {

    JwtAuthenticationResponse signUp(SignUpRequest request);

    JwtAuthenticationResponse signIn(SignInRequest request);

    JwtAuthenticationResponse refresh(RefreshTokenRequest refreshTokenRequest);
}

package com.java.recruitment.service;

import com.java.recruitment.web.dto.auth.old.JwtAuthenticationResponse;
import com.java.recruitment.web.dto.auth.old.RefreshTokenRequest;
import com.java.recruitment.web.dto.auth.old.SignInRequest;

public interface IAuthenticationService {

    JwtAuthenticationResponse signUp(SignUpRequest request);

    JwtAuthenticationResponse signIn(SignInRequest request);

    JwtAuthenticationResponse refresh(RefreshTokenRequest refreshTokenRequest);
}

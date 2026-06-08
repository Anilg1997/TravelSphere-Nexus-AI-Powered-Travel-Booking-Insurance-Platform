package com.travelsphere.auth.service;

import com.travelsphere.auth.dto.AuthResponse;
import com.travelsphere.auth.dto.LoginRequest;
import com.travelsphere.auth.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(String refreshToken);
    void logout(String accessToken);
}

package com.travelsphere.auth.controller;

import com.travelsphere.auth.dto.AuthResponse;
import com.travelsphere.auth.dto.LoginRequest;
import com.travelsphere.auth.dto.RegisterRequest;
import com.travelsphere.auth.service.AuthService;
import com.travelsphere.common.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock private AuthService authService;
    @InjectMocks private AuthController authController;

    @Test
    void registerReturnsSuccessResponse() {
        RegisterRequest request = RegisterRequest.builder()
                .email("test@email.com").password("password123").fullName("Test").build();
        AuthResponse authResponse = AuthResponse.builder()
                .userId(UUID.randomUUID()).email("test@email.com").fullName("Test")
                .roles(Set.of("ROLE_USER")).accessToken("token").refreshToken("refresh")
                .expiresIn(900000).tier("SILVER").build();

        when(authService.register(request)).thenReturn(authResponse);

        ResponseEntity<ApiResponse<AuthResponse>> response = authController.register(request);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isSuccess());
        assertEquals("test@email.com", response.getBody().getData().getEmail());
        assertEquals("User registered successfully", response.getBody().getMessage());
    }

    @Test
    void loginReturnsSuccessResponse() {
        LoginRequest request = LoginRequest.builder()
                .email("test@email.com").password("password123").build();
        AuthResponse authResponse = AuthResponse.builder()
                .userId(UUID.randomUUID()).email("test@email.com")
                .accessToken("token").refreshToken("refresh")
                .expiresIn(900000).tier("SILVER").build();

        when(authService.login(request)).thenReturn(authResponse);

        ResponseEntity<ApiResponse<AuthResponse>> response = authController.login(request);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Login successful", response.getBody().getMessage());
    }

    @Test
    void refreshReturnsNewTokens() {
        AuthResponse authResponse = AuthResponse.builder()
                .userId(UUID.randomUUID()).email("test@email.com")
                .accessToken("new-token").refreshToken("old-refresh")
                .expiresIn(900000).tier("SILVER").build();

        when(authService.refreshToken("old-refresh")).thenReturn(authResponse);

        ResponseEntity<ApiResponse<AuthResponse>> response = authController.refresh("old-refresh");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("new-token", response.getBody().getData().getAccessToken());
    }

    @Test
    void logoutReturnsSuccess() {
        ResponseEntity<ApiResponse<Void>> response = authController.logout("Bearer some-token");

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isSuccess());
        verify(authService).logout("some-token");
    }

    @Test
    void meReturnsUserInfo() {
        UUID userId = UUID.randomUUID();
        ResponseEntity<ApiResponse<AuthResponse>> response = authController.me(
                userId.toString(), "test@email.com", "ROLE_USER");

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Current user info", response.getBody().getMessage());
    }
}

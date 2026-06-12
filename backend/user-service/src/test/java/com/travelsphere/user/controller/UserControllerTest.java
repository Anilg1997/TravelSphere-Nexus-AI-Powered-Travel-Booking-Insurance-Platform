package com.travelsphere.user.controller;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.user.dto.*;
import com.travelsphere.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock private UserService userService;
    @InjectMocks private UserController userController;

    @Test
    void getProfileReturnsProfile() {
        UserProfileResponse profile = UserProfileResponse.builder()
                .userId(UUID.randomUUID()).fullName("John Doe")
                .loyaltyPoints(500).loyaltyTier("GOLD").totalTrips(5).build();
        when(userService.getProfile(any())).thenReturn(profile);

        ResponseEntity<ApiResponse<UserProfileResponse>> response =
                userController.getProfile(UUID.randomUUID().toString());

        assertEquals(200, response.getStatusCode().value());
        assertEquals("John Doe", response.getBody().getData().getFullName());
    }

    @Test
    void updateProfileReturnsUpdated() {
        UserProfileResponse profile = UserProfileResponse.builder()
                .userId(UUID.randomUUID()).fullName("Jane Doe").build();
        when(userService.updateProfile(any(), any())).thenReturn(profile);

        UpdateProfileRequest request = UpdateProfileRequest.builder().fullName("Jane Doe").build();

        ResponseEntity<ApiResponse<UserProfileResponse>> response =
                userController.updateProfile(UUID.randomUUID().toString(), request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Jane Doe", response.getBody().getData().getFullName());
    }

    @Test
    void getLoyaltyHistoryReturnsList() {
        LoyaltyTransactionResponse tx = LoyaltyTransactionResponse.builder()
                .id(UUID.randomUUID()).points(100).type("BONUS")
                .description("Welcome bonus").createdAt(LocalDateTime.now()).build();
        when(userService.getLoyaltyHistory(any())).thenReturn(List.of(tx));

        ResponseEntity<ApiResponse<List<LoyaltyTransactionResponse>>> response =
                userController.getLoyaltyHistory(UUID.randomUUID().toString());

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void createReferralReturnsReferral() {
        ReferralResponse referral = ReferralResponse.builder()
                .id(UUID.randomUUID()).referralCode("TS-ABC123")
                .status("PENDING").bonusPointsAwarded(500).build();
        when(userService.createReferral(any(), any())).thenReturn(referral);

        ReferralRequest request = ReferralRequest.builder()
                .referredEmail("friend@email.com").build();

        ResponseEntity<ApiResponse<ReferralResponse>> response =
                userController.createReferral(UUID.randomUUID().toString(), request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("TS-ABC123", response.getBody().getData().getReferralCode());
    }

    @Test
    void getMyReferralsReturnsList() {
        when(userService.getMyReferrals(any())).thenReturn(List.of());

        ResponseEntity<ApiResponse<List<ReferralResponse>>> response =
                userController.getMyReferrals(UUID.randomUUID().toString());

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().getData().isEmpty());
    }
}

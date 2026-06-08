package com.travelsphere.notification.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationRequest {
    @NotBlank
    private String userId;

    @NotBlank
    private String type;

    @NotBlank
    private String channel;

    @NotBlank
    private String title;

    @NotBlank
    private String message;
}

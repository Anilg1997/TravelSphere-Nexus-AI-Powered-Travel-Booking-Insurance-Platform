package com.travelsphere.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private String id;
    private UUID userId;
    private String type;
    private String channel;
    private String title;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
}

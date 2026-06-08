package com.travelsphere.notification.controller;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.notification.dto.*;
import com.travelsphere.notification.model.Notification;
import com.travelsphere.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Email, SMS, and WebSocket push notification APIs")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Get all notifications for current user")
    public ResponseEntity<ApiResponse<List<Notification>>> getNotifications(
            @RequestHeader("X-User-Id") String userId) {
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(ApiResponse.success(notifications));
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Mark notification as read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Notification marked as read"));
    }

    @PostMapping("/send")
    @Operation(summary = "Send a notification (admin)")
    public ResponseEntity<ApiResponse<Void>> sendNotification(
            @Valid @RequestBody SendNotificationRequest request) {
        notificationService.sendNotification(request);
        return ResponseEntity.ok(ApiResponse.success(null, "Notification sent successfully"));
    }
}

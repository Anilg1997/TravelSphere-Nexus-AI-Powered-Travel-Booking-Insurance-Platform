package com.travelsphere.notification.service;

import com.travelsphere.notification.dto.*;
import com.travelsphere.notification.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final StringRedisTemplate redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String NOTIFICATION_KEY_PREFIX = "notifications:";

    @Override
    public List<Notification> getUserNotifications(String userId) {
        String key = NOTIFICATION_KEY_PREFIX + userId;
        List<String> notifications = redisTemplate.opsForList().range(key, 0, -1);
        if (notifications == null) return new ArrayList<>();

        return notifications.stream()
                .map(n -> deserializeNotification(n))
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(String notificationId) {
        // In production, would update in database
        log.info("Marking notification {} as read", notificationId);
    }

    @Override
    public void sendNotification(SendNotificationRequest request) {
        Notification notification = Notification.builder()
                .userId(UUID.fromString(request.getUserId()))
                .type(request.getType())
                .channel(request.getChannel())
                .title(request.getTitle())
                .message(request.getMessage())
                .build();

        // Store in Redis
        String key = NOTIFICATION_KEY_PREFIX + request.getUserId();
        redisTemplate.opsForList().leftPush(key, serializeNotification(notification));
        redisTemplate.expire(key, java.time.Duration.ofDays(30));

        // Send email if channel is EMAIL
        if ("EMAIL".equalsIgnoreCase(request.getChannel())) {
            sendEmail(request);
        }

        // Send WebSocket push
        messagingTemplate.convertAndSend(
                "/topic/notifications/" + request.getUserId(),
                notification
        );

        log.info("Notification sent to user {}: {}", request.getUserId(), request.getTitle());
    }

    @Override
    @KafkaListener(topics = "ts.notifications.send", groupId = "notification-service-group")
    public void processKafkaNotification(String payload) {
        log.info("Received Kafka notification: {}", payload);
        // Parse and send notification
        SendNotificationRequest request = SendNotificationRequest.builder()
                .userId("system")
                .type("SYSTEM")
                .channel("WEBSOCKET")
                .title("System Notification")
                .message(payload)
                .build();
        sendNotification(request);
    }

    private void sendEmail(SendNotificationRequest request) {
        log.info("Sending email to user {}: {}", request.getUserId(), request.getTitle());
        // In production, use JavaMailSender with Thymeleaf templates
    }

    private String serializeNotification(Notification notification) {
        try {
            return com.fasterxml.jackson.databind.ObjectMapper.class.getDeclaredConstructor()
                    .newInstance().writeValueAsString(notification);
        } catch (Exception e) {
            return notification.toString();
        }
    }

    private Notification deserializeNotification(String json) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.readValue(json, Notification.class);
        } catch (Exception e) {
            return Notification.builder().title("Notification").message(json).build();
        }
    }
}

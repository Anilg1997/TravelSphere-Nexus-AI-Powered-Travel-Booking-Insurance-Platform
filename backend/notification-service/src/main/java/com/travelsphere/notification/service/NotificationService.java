package com.travelsphere.notification.service;

import com.travelsphere.notification.dto.*;
import com.travelsphere.notification.model.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> getUserNotifications(String userId);
    void markAsRead(String notificationId);
    void sendNotification(SendNotificationRequest request);
    void processKafkaNotification(String payload);
}

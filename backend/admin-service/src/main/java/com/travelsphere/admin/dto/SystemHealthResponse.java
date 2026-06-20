package com.travelsphere.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemHealthResponse {
    private String overallStatus;
    private long uptime;
    private Map<String, ServiceHealth> services;
    private DatabaseHealth database;
    private CacheHealth cache;
    private MessagingHealth messaging;
    private List<Alert> recentAlerts;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ServiceHealth {
        private String name;
        private String status;
        private long responseTimeMs;
        private int instanceCount;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class DatabaseHealth {
        private String status;
        private long activeConnections;
        private long totalConnections;
        private double averageQueryTimeMs;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class CacheHealth {
        private String status;
        private long hitRate;
        private long memoryUsage;
        private long keysCount;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class MessagingHealth {
        private String status;
        private long messagesPerSecond;
        private long consumerLag;
        private long queueDepth;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Alert {
        private String type;
        private String message;
        private String severity;
        private String timestamp;
    }
}

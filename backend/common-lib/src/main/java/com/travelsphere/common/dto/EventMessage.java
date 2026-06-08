package com.travelsphere.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventMessage {
    private String eventId;
    private String eventType;
    private String sourceService;
    private String userId;
    private String referenceId;
    private Object payload;
    private String timestamp;

    public static EventMessage of(String eventType, String sourceService, String userId, Object payload) {
        return EventMessage.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(eventType)
                .sourceService(sourceService)
                .userId(userId)
                .payload(payload)
                .timestamp(Instant.now().toString())
                .build();
    }
}

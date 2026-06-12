package com.travelsphere.common.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventMessageTest {

    @Test
    void ofFactoryMethodCreatesEvent() {
        Object payload = "test-payload";
        EventMessage event = EventMessage.of("USER_REGISTERED", "auth-service", "user-123", payload);

        assertNotNull(event.getEventId());
        assertEquals("USER_REGISTERED", event.getEventType());
        assertEquals("auth-service", event.getSourceService());
        assertEquals("user-123", event.getUserId());
        assertEquals(payload, event.getPayload());
        assertNotNull(event.getTimestamp());
    }

    @Test
    void ofGeneratesUniqueEventIds() {
        EventMessage e1 = EventMessage.of("TYPE", "svc", "u1", null);
        EventMessage e2 = EventMessage.of("TYPE", "svc", "u1", null);

        assertNotEquals(e1.getEventId(), e2.getEventId());
    }

    @Test
    void builderPatternWorks() {
        EventMessage event = EventMessage.builder()
                .eventId("evt-001")
                .eventType("FLIGHT_BOOKED")
                .sourceService("flight-service")
                .userId("user-abc")
                .referenceId("booking-123")
                .payload("some-data")
                .timestamp("2024-01-15T10:30:00Z")
                .build();

        assertEquals("evt-001", event.getEventId());
        assertEquals("FLIGHT_BOOKED", event.getEventType());
        assertEquals("flight-service", event.getSourceService());
        assertEquals("user-abc", event.getUserId());
        assertEquals("booking-123", event.getReferenceId());
        assertEquals("some-data", event.getPayload());
    }

    @Test
    void nullFieldsAreAllowed() {
        EventMessage event = EventMessage.of("TYPE", "svc", null, null);

        assertNull(event.getUserId());
        assertNull(event.getPayload());
        assertNotNull(event.getEventId());
    }
}

package com.travelsphere.common.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void successWithDataAndMessage() {
        ApiResponse<String> response = ApiResponse.success("test-data", "Operation successful");

        assertTrue(response.isSuccess());
        assertEquals("test-data", response.getData());
        assertEquals("Operation successful", response.getMessage());
        assertNotNull(response.getTimestamp());
        assertNotNull(response.getRequestId());
        assertNull(response.getError());
    }

    @Test
    void successWithDataOnly() {
        ApiResponse<Integer> response = ApiResponse.success(42);

        assertTrue(response.isSuccess());
        assertEquals(42, response.getData());
        assertNull(response.getMessage());
        assertNull(response.getError());
        assertNotNull(response.getTimestamp());
        assertNotNull(response.getRequestId());
    }

    @Test
    void errorWithMessage() {
        ApiResponse<Void> response = ApiResponse.error("INVALID_INPUT", "Bad request");

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertEquals("INVALID_INPUT", response.getError());
        assertEquals("Bad request", response.getMessage());
        assertNotNull(response.getTimestamp());
        assertNotNull(response.getRequestId());
    }

    @Test
    void errorWithSingleMessage() {
        ApiResponse<Void> response = ApiResponse.error("NOT_FOUND");

        assertFalse(response.isSuccess());
        assertEquals("NOT_FOUND", response.getError());
        assertNull(response.getMessage());
        assertNotNull(response.getTimestamp());
        assertNotNull(response.getRequestId());
    }

    @Test
    void builderPatternWorks() {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .data("hello")
                .message("ok")
                .error(null)
                .timestamp("2024-01-15T10:30:00Z")
                .requestId("test-uuid")
                .build();

        assertTrue(response.isSuccess());
        assertEquals("hello", response.getData());
        assertEquals("ok", response.getMessage());
        assertEquals("2024-01-15T10:30:00Z", response.getTimestamp());
        assertEquals("test-uuid", response.getRequestId());
    }

    @Test
    void requestIdsAreUnique() {
        ApiResponse<Void> r1 = ApiResponse.error("ERR");
        ApiResponse<Void> r2 = ApiResponse.error("ERR");

        assertNotEquals(r1.getRequestId(), r2.getRequestId());
    }

    @Test
    void timestampsAreNotNull() {
        ApiResponse<String> response = ApiResponse.success("data");

        assertNotNull(response.getTimestamp());
        assertTrue(response.getTimestamp().contains("T"));
    }
}

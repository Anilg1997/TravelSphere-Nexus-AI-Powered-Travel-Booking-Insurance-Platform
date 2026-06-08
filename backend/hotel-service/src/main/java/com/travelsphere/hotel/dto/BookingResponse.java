package com.travelsphere.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BookingResponse {
    private String bookingRef;
    private String hotelName;
    private String roomTypeName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int guests;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime bookedAt;
    private String specialRequests;
}

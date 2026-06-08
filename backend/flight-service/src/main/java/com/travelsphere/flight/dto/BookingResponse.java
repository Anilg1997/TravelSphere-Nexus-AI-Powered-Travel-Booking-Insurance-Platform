package com.travelsphere.flight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BookingResponse {
    private String bookingRef;
    private String pnr;
    private String flightNumber;
    private String passengerName;
    private String seatNumber;
    private String cabinClass;
    private BigDecimal pricePaid;
    private String status;
    private LocalDateTime bookedAt;
}

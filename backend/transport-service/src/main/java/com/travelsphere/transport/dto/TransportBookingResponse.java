package com.travelsphere.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransportBookingResponse {
    private String bookingRef;
    private String pnr;
    private String routeName;
    private String operator;
    private String passengerName;
    private String seatNumber;
    private BigDecimal pricePaid;
    private String status;
    private LocalDateTime bookedAt;
}

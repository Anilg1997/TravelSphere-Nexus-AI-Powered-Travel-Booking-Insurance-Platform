package com.travelsphere.package_.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageBookingResponse {
    private String bookingRef;
    private String packageName;
    private String destination;
    private LocalDate travelDate;
    private int groupSize;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime bookedAt;
}

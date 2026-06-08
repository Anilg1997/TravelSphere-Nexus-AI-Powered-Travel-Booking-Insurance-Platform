package com.travelsphere.flight.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BookingRequest {
    @NotNull
    private UUID flightId;

    @NotBlank
    private String passengerName;

    @NotBlank @Email
    private String passengerEmail;

    @NotBlank
    private String seatNumber;

    @NotBlank
    private String cabinClass;

    private String promoCode;
}

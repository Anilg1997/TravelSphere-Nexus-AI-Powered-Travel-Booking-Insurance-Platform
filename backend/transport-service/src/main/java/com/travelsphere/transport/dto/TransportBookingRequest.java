package com.travelsphere.transport.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransportBookingRequest {
    @NotBlank
    private UUID routeId;

    @NotBlank
    private String passengerName;

    @NotBlank
    @Email
    private String passengerEmail;

    @NotBlank
    private String seatNumber;
}

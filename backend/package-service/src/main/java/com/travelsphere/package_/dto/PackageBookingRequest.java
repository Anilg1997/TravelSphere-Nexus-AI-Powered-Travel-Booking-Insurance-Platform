package com.travelsphere.package_.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageBookingRequest {
    @NotNull
    private UUID packageId;

    @NotNull
    private LocalDate travelDate;

    @NotNull
    private int groupSize;

    private String specialRequests;
}

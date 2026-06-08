package com.travelsphere.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private long totalUsers;
    private long totalBookings;
    private double totalRevenue;
    private long activeBookings;
    private long openFraudAlerts;
    private long totalFlights;
    private long totalHotels;
    private long totalPackages;
}

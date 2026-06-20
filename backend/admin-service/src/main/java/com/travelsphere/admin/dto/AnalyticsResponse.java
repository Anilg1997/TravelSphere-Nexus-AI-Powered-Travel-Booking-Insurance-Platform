package com.travelsphere.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse {
    private RevenueAnalytics revenue;
    private UserAnalytics users;
    private BookingAnalytics bookings;
    private Map<String, Long> serviceDistribution;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class RevenueAnalytics {
        private Double totalRevenue;
        private Double monthlyRevenue;
        private Double dailyAverage;
        private Double projectedMonthly;
        private List<DataPoint> revenueByDay;
        private List<DataPoint> revenueByService;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class UserAnalytics {
        private Long totalUsers;
        private Long newUsersToday;
        private Long newUsersThisMonth;
        private Long activeUsers;
        private Double growthRate;
        private List<DataPoint> userGrowthByDay;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class BookingAnalytics {
        private Long totalBookings;
        private Long bookingsToday;
        private Long bookingsThisMonth;
        private Double conversionRate;
        private Long cancelledBookings;
        private Double cancellationRate;
        private List<DataPoint> bookingsByDay;
        private List<DataPoint> bookingsByService;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class DataPoint {
        private String label;
        private Number value;
    }
}

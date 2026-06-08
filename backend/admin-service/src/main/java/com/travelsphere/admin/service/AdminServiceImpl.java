package com.travelsphere.admin.service;

import com.travelsphere.admin.dto.*;
import com.travelsphere.admin.model.AdminAuditLog;
import com.travelsphere.admin.model.FraudAlert;
import com.travelsphere.admin.repository.AdminAuditLogRepository;
import com.travelsphere.admin.repository.FraudAlertRepository;
import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final FraudAlertRepository fraudAlertRepository;
    private final AdminAuditLogRepository auditLogRepository;
    private final StringRedisTemplate redisTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // Feign clients for cross-service data aggregation
    private final FlightClient flightClient;
    private final HotelClient hotelClient;
    private final PackageClient packageClient;

    @Override
    public DashboardResponse getDashboard() {
        long openAlerts = fraudAlertRepository.countByStatus(FraudAlert.AlertStatus.OPEN);

        // Aggregate data from other services via Feign clients
        long totalFlights = countBookings(flightClient, "flight");
        long totalHotels = countBookings(hotelClient, "hotel");
        long totalPackages = countBookings(packageClient, "package");

        return DashboardResponse.builder()
                .totalUsers(0L)
                .totalBookings(totalFlights + totalHotels + totalPackages)
                .totalRevenue(0.0)
                .activeBookings(0L)
                .openFraudAlerts(openAlerts)
                .totalFlights(totalFlights)
                .totalHotels(totalHotels)
                .totalPackages(totalPackages)
                .build();
    }

    @Override
    public List<FraudAlertResponse> getFraudAlerts(String status) {
        List<FraudAlert> alerts;
        if (status != null && !status.isBlank()) {
            alerts = fraudAlertRepository.findByStatusOrderByCreatedAtDesc(
                    FraudAlert.AlertStatus.valueOf(status.toUpperCase()));
        } else {
            alerts = fraudAlertRepository.findAllByOrderByCreatedAtDesc();
        }
        return alerts.stream().map(this::toFraudAlertResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FraudAlertResponse updateFraudAlert(String alertId, UpdateFraudAlertRequest request) {
        FraudAlert alert = fraudAlertRepository.findById(UUID.fromString(alertId))
                .orElseThrow(() -> new IllegalArgumentException("Fraud alert not found"));

        if (request.getStatus() != null) {
            alert.setStatus(FraudAlert.AlertStatus.valueOf(request.getStatus().toUpperCase()));
        }
        if (alert.getStatus() == FraudAlert.AlertStatus.RESOLVED) {
            alert.setResolvedAt(LocalDateTime.now());
        }

        alert = fraudAlertRepository.save(alert);
        return toFraudAlertResponse(alert);
    }

    @Override
    @Transactional
    public void createAuditLog(String adminUserId, String action, String entityType, String entityId, String details) {
        AdminAuditLog logEntry = AdminAuditLog.builder()
                .adminUserId(adminUserId != null ? UUID.fromString(adminUserId) : null)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .details(details)
                .build();
        auditLogRepository.save(logEntry);
        log.info("Audit log created: {} {} {} by {}", action, entityType, entityId, adminUserId);
    }

    private long countBookings(Object client, String serviceType) {
        // TODO: Replace with actual count endpoints from each service
        // For now, return 0 as dashboard is aggregated from local data
        log.debug("Dashboard count for {} service - using local data", serviceType);
        return 0L;
    }

    private FraudAlertResponse toFraudAlertResponse(FraudAlert alert) {
        return FraudAlertResponse.builder()
                .id(alert.getId())
                .userId(alert.getUserId())
                .alertType(alert.getAlertType())
                .description(alert.getDescription())
                .severity(alert.getSeverity().name())
                .status(alert.getStatus().name())
                .referenceId(alert.getReferenceId())
                .createdAt(alert.getCreatedAt())
                .resolvedAt(alert.getResolvedAt())
                .build();
    }
}

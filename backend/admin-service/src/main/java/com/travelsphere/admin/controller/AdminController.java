package com.travelsphere.admin.controller;

import com.travelsphere.admin.dto.*;
import com.travelsphere.admin.service.AdminService;
import com.travelsphere.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Dashboard, analytics, fraud alerts, and audit APIs")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    @Operation(summary = "Get admin dashboard summary")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard() {
        DashboardResponse dashboard = adminService.getDashboard();
        return ResponseEntity.ok(ApiResponse.success(dashboard));
    }

    @GetMapping("/fraud-alerts")
    @Operation(summary = "Get all fraud alerts")
    public ResponseEntity<ApiResponse<List<FraudAlertResponse>>> getFraudAlerts(
            @RequestParam(required = false) String status) {
        List<FraudAlertResponse> alerts = adminService.getFraudAlerts(status);
        return ResponseEntity.ok(ApiResponse.success(alerts));
    }

    @PutMapping("/fraud-alerts/{id}")
    @Operation(summary = "Update fraud alert status")
    public ResponseEntity<ApiResponse<FraudAlertResponse>> updateFraudAlert(
            @PathVariable String id,
            @Valid @RequestBody UpdateFraudAlertRequest request) {
        FraudAlertResponse alert = adminService.updateFraudAlert(id, request);
        return ResponseEntity.ok(ApiResponse.success(alert, "Fraud alert updated"));
    }

    @PostMapping("/audit-log")
    @Operation(summary = "Create audit log entry")
    public ResponseEntity<ApiResponse<Void>> createAuditLog(
            @RequestHeader("X-User-Id") String adminUserId,
            @RequestParam String action,
            @RequestParam String entityType,
            @RequestParam String entityId,
            @RequestParam(required = false) String details) {
        adminService.createAuditLog(adminUserId, action, entityType, entityId, details);
        return ResponseEntity.ok(ApiResponse.success(null, "Audit log created"));
    }
}

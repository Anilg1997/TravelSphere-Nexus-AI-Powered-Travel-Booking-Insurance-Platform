package com.travelsphere.admin.service;

import com.travelsphere.admin.dto.*;

import java.util.List;

public interface AdminService {
    DashboardResponse getDashboard();
    List<FraudAlertResponse> getFraudAlerts(String status);
    FraudAlertResponse updateFraudAlert(String alertId, UpdateFraudAlertRequest request);
    void createAuditLog(String adminUserId, String action, String entityType, String entityId, String details);
}

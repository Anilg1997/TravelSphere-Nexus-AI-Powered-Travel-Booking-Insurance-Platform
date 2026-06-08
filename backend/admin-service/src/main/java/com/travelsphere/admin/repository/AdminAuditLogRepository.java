package com.travelsphere.admin.repository;

import com.travelsphere.admin.model.AdminAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminAuditLogRepository extends JpaRepository<AdminAuditLog, UUID> {
    List<AdminAuditLog> findByAdminUserIdOrderByCreatedAtDesc(UUID adminUserId);
}

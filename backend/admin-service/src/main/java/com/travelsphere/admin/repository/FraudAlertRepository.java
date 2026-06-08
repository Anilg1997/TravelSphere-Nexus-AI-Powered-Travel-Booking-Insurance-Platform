package com.travelsphere.admin.repository;

import com.travelsphere.admin.model.FraudAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FraudAlertRepository extends JpaRepository<FraudAlert, UUID> {
    List<FraudAlert> findByStatusOrderByCreatedAtDesc(FraudAlert.AlertStatus status);
    List<FraudAlert> findAllByOrderByCreatedAtDesc();
    long countByStatus(FraudAlert.AlertStatus status);
}

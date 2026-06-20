package com.travelsphere.admin.repository;

import com.travelsphere.admin.model.SystemMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SystemMetricRepository extends JpaRepository<SystemMetric, UUID> {
    List<SystemMetric> findByServiceNameAndRecordedAtAfterOrderByRecordedAtDesc(
            String serviceName, LocalDateTime after);
    List<SystemMetric> findByMetricNameAndRecordedAtAfterOrderByRecordedAtDesc(
            String metricName, LocalDateTime after);
    List<SystemMetric> findByServiceName(String serviceName);
}

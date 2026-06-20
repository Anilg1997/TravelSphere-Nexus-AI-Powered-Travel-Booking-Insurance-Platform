package com.travelsphere.admin.repository;

import com.travelsphere.admin.model.BookingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRecordRepository extends JpaRepository<BookingRecord, UUID> {
    List<BookingRecord> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<BookingRecord> findByServiceTypeOrderByCreatedAtDesc(String serviceType);
    List<BookingRecord> findAllByOrderByCreatedAtDesc();
    List<BookingRecord> findByCreatedAtBetweenOrderByCreatedAtAsc(LocalDateTime start, LocalDateTime end);
    long countByServiceType(String serviceType);
    long countByStatus(String status);
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    long countByPaymentStatus(String paymentStatus);
}

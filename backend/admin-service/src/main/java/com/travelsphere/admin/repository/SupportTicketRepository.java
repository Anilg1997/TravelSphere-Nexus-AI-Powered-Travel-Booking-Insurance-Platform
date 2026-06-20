package com.travelsphere.admin.repository;

import com.travelsphere.admin.model.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, UUID> {
    List<SupportTicket> findByStatusOrderByCreatedAtDesc(SupportTicket.TicketStatus status);
    List<SupportTicket> findAllByOrderByCreatedAtDesc();
    List<SupportTicket> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<SupportTicket> findByPriorityOrderByCreatedAtDesc(SupportTicket.TicketPriority priority);
    List<SupportTicket> findByCategoryOrderByCreatedAtDesc(SupportTicket.TicketCategory category);
    long countByStatus(SupportTicket.TicketStatus status);
    long countByPriority(SupportTicket.TicketPriority priority);
}

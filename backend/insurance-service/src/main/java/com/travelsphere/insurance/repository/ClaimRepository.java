package com.travelsphere.insurance.repository;

import com.travelsphere.insurance.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, UUID> {
    Optional<Claim> findByClaimNumber(String claimNumber);
    List<Claim> findByUserId(UUID userId);
    List<Claim> findByPolicyId(UUID policyId);
    List<Claim> findByStatus(String status);
}

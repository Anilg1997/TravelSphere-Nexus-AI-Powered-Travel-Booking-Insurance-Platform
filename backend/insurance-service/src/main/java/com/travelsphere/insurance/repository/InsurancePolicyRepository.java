package com.travelsphere.insurance.repository;

import com.travelsphere.insurance.model.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, UUID> {
    Optional<InsurancePolicy> findByPolicyNumber(String policyNumber);
    List<InsurancePolicy> findByUserId(UUID userId);
}

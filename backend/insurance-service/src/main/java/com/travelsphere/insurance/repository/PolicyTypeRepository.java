package com.travelsphere.insurance.repository;

import com.travelsphere.insurance.model.PolicyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PolicyTypeRepository extends JpaRepository<PolicyType, UUID> {
    List<PolicyType> findByIsActiveTrue();
}

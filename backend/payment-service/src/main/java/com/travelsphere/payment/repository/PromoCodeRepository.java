package com.travelsphere.payment.repository;

import com.travelsphere.payment.model.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, String> {
    Optional<PromoCode> findByCodeAndIsActiveTrue(String code);
}

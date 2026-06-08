package com.travelsphere.user.repository;

import com.travelsphere.user.model.Referral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, UUID> {
    List<Referral> findByReferrerUserId(UUID referrerUserId);
    Referral findByReferralCode(String referralCode);
    List<Referral> findByReferredEmail(String email);
}

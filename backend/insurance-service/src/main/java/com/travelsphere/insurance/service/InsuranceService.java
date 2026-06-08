package com.travelsphere.insurance.service;

import com.travelsphere.insurance.dto.*;

import java.util.List;

public interface InsuranceService {
    List<PolicyTypeResponse> getAvailablePolicies();
    PremiumResponse calculatePremium(PremiumRequest request);
    PurchaseResponse purchasePolicy(PurchaseRequest request, String userId);
    List<PurchaseResponse> getMyPolicies(String userId);
    ClaimResponse fileClaim(ClaimRequest request, String userId);
    ClaimResponse getClaim(String claimId);
    List<ClaimResponse> getMyClaims(String userId);
    ClaimResponse reviewClaim(String claimId, String status, String notes);
    ClaimResponse resolveClaim(String claimId, String resolution, String notes);
}

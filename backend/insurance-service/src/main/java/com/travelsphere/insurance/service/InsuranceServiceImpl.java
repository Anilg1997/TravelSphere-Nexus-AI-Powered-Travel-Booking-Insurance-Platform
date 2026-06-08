package com.travelsphere.insurance.service;

import com.travelsphere.insurance.dto.*;
import com.travelsphere.insurance.model.*;
import com.travelsphere.insurance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InsuranceServiceImpl implements InsuranceService {

    private final PolicyTypeRepository policyTypeRepository;
    private final InsurancePolicyRepository policyRepository;
    private final ClaimRepository claimRepository;
    private final ClaimDocumentRepository claimDocumentRepository;
    private final InsurancePremiumEngine premiumEngine;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public List<PolicyTypeResponse> getAvailablePolicies() {
        return policyTypeRepository.findByIsActiveTrue().stream()
                .map(this::toPolicyTypeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PremiumResponse calculatePremium(PremiumRequest request) {
        PolicyType policyType = policyTypeRepository.findById(request.getPolicyTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Policy type not found"));

        var result = premiumEngine.calculatePremium(policyType, request.getDestination(),
                request.getDurationDays(), request.getTravellerAge());

        return PremiumResponse.builder()
                .policyTypeName(policyType.getName())
                .destination(request.getDestination())
                .durationDays(request.getDurationDays())
                .travellerAge(request.getTravellerAge())
                .basePremium(policyType.getBasePremium())
                .calculatedPremium(result.calculatedPremium())
                .destinationMultiplier(result.destinationMultiplier())
                .ageMultiplier(result.ageMultiplier())
                .durationMultiplier(result.durationMultiplier())
                .maxCoverage(policyType.getMaxCoverage())
                .build();
    }

    @Override
    @Transactional
    public PurchaseResponse purchasePolicy(PurchaseRequest request, String userId) {
        PolicyType policyType = policyTypeRepository.findById(request.getPolicyTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Policy type not found"));

        long days = ChronoUnit.DAYS.between(request.getTripStart(), request.getTripEnd());
        if (days < 1) throw new IllegalArgumentException("Trip must be at least 1 day");

        var result = premiumEngine.calculatePremium(policyType, request.getTripDestination(),
                (int) days, request.getTravellerAge());

        String policyNumber = "TS-INS-" + java.time.Year.now().getValue() + "-"
                + String.format("%06d", new Random().nextInt(999999));

        InsurancePolicy policy = InsurancePolicy.builder()
                .policyNumber(policyNumber)
                .userId(userId != null ? UUID.fromString(userId) : null)
                .policyTypeId(policyType.getId())
                .bookingRef(request.getBookingRef())
                .tripDestination(request.getTripDestination())
                .tripStart(request.getTripStart())
                .tripEnd(request.getTripEnd())
                .insuredAmount(policyType.getMaxCoverage())
                .premiumPaid(result.calculatedPremium())
                .status("ACTIVE")
                .travellerAge(request.getTravellerAge())
                .build();

        policyRepository.save(policy);

        kafkaTemplate.send("ts.insurance.policy-issued", policyNumber, policy);

        return PurchaseResponse.builder()
                .policyNumber(policyNumber)
                .policyTypeName(policyType.getName())
                .tripDestination(request.getTripDestination())
                .tripStart(request.getTripStart())
                .tripEnd(request.getTripEnd())
                .insuredAmount(policyType.getMaxCoverage())
                .premiumPaid(result.calculatedPremium())
                .status("ACTIVE")
                .issuedAt(policy.getIssuedAt())
                .build();
    }

    @Override
    public List<PurchaseResponse> getMyPolicies(String userId) {
        return policyRepository.findByUserId(UUID.fromString(userId)).stream()
                .map(p -> {
                    PolicyType pt = policyTypeRepository.findById(p.getPolicyTypeId()).orElse(null);
                    return PurchaseResponse.builder()
                            .policyNumber(p.getPolicyNumber())
                            .policyTypeName(pt != null ? pt.getName() : "Unknown")
                            .tripDestination(p.getTripDestination())
                            .tripStart(p.getTripStart())
                            .tripEnd(p.getTripEnd())
                            .insuredAmount(p.getInsuredAmount())
                            .premiumPaid(p.getPremiumPaid())
                            .status(p.getStatus())
                            .issuedAt(p.getIssuedAt())
                            .build();
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClaimResponse fileClaim(ClaimRequest request, String userId) {
        InsurancePolicy policy = policyRepository.findByPolicyNumber(request.getPolicyNumber())
                .orElseThrow(() -> new IllegalArgumentException("Policy not found"));

        if (!"ACTIVE".equals(policy.getStatus())) {
            throw new IllegalStateException("Policy is not active. Current status: " + policy.getStatus());
        }

        String claimNumber = "TS-CLM-" + java.time.Year.now().getValue() + "-"
                + String.format("%06d", new Random().nextInt(999999));

        Claim claim = Claim.builder()
                .claimNumber(claimNumber)
                .policyId(policy.getId())
                .userId(userId != null ? UUID.fromString(userId) : null)
                .claimType(request.getClaimType())
                .incidentDate(request.getIncidentDate())
                .description(request.getDescription())
                .claimAmount(request.getClaimAmount())
                .status("PENDING")
                .build();

        claimRepository.save(claim);

        kafkaTemplate.send("ts.insurance.claim-filed", claimNumber, claim);

        return toClaimResponse(claim, policy.getPolicyNumber());
    }

    @Override
    public ClaimResponse getClaim(String claimId) {
        Claim claim = claimRepository.findById(UUID.fromString(claimId))
                .orElseThrow(() -> new IllegalArgumentException("Claim not found"));

        InsurancePolicy policy = policyRepository.findById(claim.getPolicyId()).orElse(null);
        return toClaimResponse(claim, policy != null ? policy.getPolicyNumber() : "Unknown");
    }

    @Override
    public List<ClaimResponse> getMyClaims(String userId) {
        return claimRepository.findByUserId(UUID.fromString(userId)).stream()
                .map(c -> {
                    InsurancePolicy p = policyRepository.findById(c.getPolicyId()).orElse(null);
                    return toClaimResponse(c, p != null ? p.getPolicyNumber() : "Unknown");
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClaimResponse reviewClaim(String claimId, String status, String notes) {
        Claim claim = claimRepository.findById(UUID.fromString(claimId))
                .orElseThrow(() -> new IllegalArgumentException("Claim not found"));

        claim.setStatus("UNDER_REVIEW");
        claim.setResolutionNotes(notes);
        claimRepository.save(claim);

        InsurancePolicy policy = policyRepository.findById(claim.getPolicyId()).orElse(null);
        return toClaimResponse(claim, policy != null ? policy.getPolicyNumber() : "Unknown");
    }

    @Override
    @Transactional
    public ClaimResponse resolveClaim(String claimId, String resolution, String notes) {
        Claim claim = claimRepository.findById(UUID.fromString(claimId))
                .orElseThrow(() -> new IllegalArgumentException("Claim not found"));

        if ("APPROVED".equals(resolution)) {
            claim.setStatus("APPROVED");
            claim.setApprovedAmount(claim.getClaimAmount());
        } else {
            claim.setStatus("REJECTED");
            claim.setApprovedAmount(BigDecimal.ZERO);
        }
        claim.setResolutionNotes(notes);
        claim.setResolvedAt(LocalDateTime.now());
        claimRepository.save(claim);

        kafkaTemplate.send("ts.insurance.claim-resolved", claim.getClaimNumber(), claim);

        InsurancePolicy policy = policyRepository.findById(claim.getPolicyId()).orElse(null);
        return toClaimResponse(claim, policy != null ? policy.getPolicyNumber() : "Unknown");
    }

    private PolicyTypeResponse toPolicyTypeResponse(PolicyType pt) {
        return PolicyTypeResponse.builder()
                .id(pt.getId()).name(pt.getName())
                .description(pt.getDescription())
                .coverageType(pt.getCoverageType())
                .basePremium(pt.getBasePremium())
                .maxCoverage(pt.getMaxCoverage())
                .build();
    }

    private ClaimResponse toClaimResponse(Claim claim, String policyNumber) {
        List<String> docs = claimDocumentRepository.findByClaimId(claim.getId()).stream()
                .map(ClaimDocument::getS3Key).collect(Collectors.toList());

        return ClaimResponse.builder()
                .id(claim.getId()).claimNumber(claim.getClaimNumber())
                .policyNumber(policyNumber).claimType(claim.getClaimType())
                .incidentDate(claim.getIncidentDate()).description(claim.getDescription())
                .claimAmount(claim.getClaimAmount()).approvedAmount(claim.getApprovedAmount())
                .status(claim.getStatus()).filedAt(claim.getFiledAt())
                .resolvedAt(claim.getResolvedAt()).resolutionNotes(claim.getResolutionNotes())
                .documents(docs).build();
    }
}

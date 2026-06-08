package com.travelsphere.insurance.controller;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.insurance.dto.*;
import com.travelsphere.insurance.service.InsuranceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/insurance")
@RequiredArgsConstructor
@Tag(name = "Insurance", description = "Travel insurance policies, premium calculation, and claims management")
public class InsuranceController {

    private final InsuranceService insuranceService;

    @GetMapping("/policies")
    @Operation(summary = "Get all available insurance policy types")
    public ResponseEntity<ApiResponse<List<PolicyTypeResponse>>> getPolicies() {
        return ResponseEntity.ok(ApiResponse.success(insuranceService.getAvailablePolicies()));
    }

    @PostMapping("/calculate")
    @Operation(summary = "Calculate premium for a trip")
    public ResponseEntity<ApiResponse<PremiumResponse>> calculatePremium(
            @Valid @RequestBody PremiumRequest request) {
        PremiumResponse response = insuranceService.calculatePremium(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Premium calculated"));
    }

    @PostMapping("/purchase")
    @Operation(summary = "Purchase an insurance policy")
    public ResponseEntity<ApiResponse<PurchaseResponse>> purchasePolicy(
            @Valid @RequestBody PurchaseRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        PurchaseResponse response = insuranceService.purchasePolicy(request, userId);
        return ResponseEntity.ok(ApiResponse.success(response, "Policy purchased successfully"));
    }

    @GetMapping("/my-policies")
    @Operation(summary = "Get current user's policies")
    public ResponseEntity<ApiResponse<List<PurchaseResponse>>> getMyPolicies(
            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(ApiResponse.success(insuranceService.getMyPolicies(userId)));
    }

    @PostMapping("/claims")
    @Operation(summary = "File a new insurance claim")
    public ResponseEntity<ApiResponse<ClaimResponse>> fileClaim(
            @Valid @RequestBody ClaimRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        ClaimResponse response = insuranceService.fileClaim(request, userId);
        return ResponseEntity.ok(ApiResponse.success(response, "Claim filed successfully"));
    }

    @GetMapping("/claims/{id}")
    @Operation(summary = "Get claim details by ID")
    public ResponseEntity<ApiResponse<ClaimResponse>> getClaim(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(insuranceService.getClaim(id)));
    }

    @GetMapping("/my-claims")
    @Operation(summary = "Get current user's claims")
    public ResponseEntity<ApiResponse<List<ClaimResponse>>> getMyClaims(
            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(ApiResponse.success(insuranceService.getMyClaims(userId)));
    }

    @PatchMapping("/claims/{id}/review")
    @Operation(summary = "Review a claim (set to UNDER_REVIEW)")
    public ResponseEntity<ApiResponse<ClaimResponse>> reviewClaim(
            @PathVariable String id,
            @RequestParam(defaultValue = "Under review") String notes) {
        return ResponseEntity.ok(ApiResponse.success(
                insuranceService.reviewClaim(id, "UNDER_REVIEW", notes)));
    }

    @PostMapping("/claims/{id}/resolve")
    @Operation(summary = "Approve or reject a claim")
    public ResponseEntity<ApiResponse<ClaimResponse>> resolveClaim(
            @PathVariable String id,
            @RequestParam String resolution,
            @RequestParam String notes) {
        return ResponseEntity.ok(ApiResponse.success(
                insuranceService.resolveClaim(id, resolution, notes), "Claim resolved"));
    }
}

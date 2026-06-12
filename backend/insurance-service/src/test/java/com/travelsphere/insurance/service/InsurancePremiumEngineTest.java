package com.travelsphere.insurance.service;

import com.travelsphere.insurance.model.PolicyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InsurancePremiumEngineTest {

    private InsurancePremiumEngine engine;

    @BeforeEach
    void setUp() {
        engine = new InsurancePremiumEngine();
    }

    private PolicyType createPolicyType(BigDecimal basePremium, BigDecimal maxCoverage) {
        return PolicyType.builder()
                .id(UUID.randomUUID())
                .name("Basic Travel")
                .description("Basic travel insurance")
                .coverageType(new String[]{"BASIC", "TRIP_CANCELLATION"})
                .basePremium(basePremium)
                .maxCoverage(maxCoverage)
                .isActive(true)
                .build();
    }

    @Test
    void domesticTripHasLowerMultiplier() {
        PolicyType policy = createPolicyType(new BigDecimal("100.00"), new BigDecimal("500000"));

        InsurancePremiumEngine.PremiumResult result = engine.calculatePremium(policy, "Domestic", 7, 30);

        assertEquals(0.9, result.destinationMultiplier(), 0.01);
        // base=100 * dest=0.9 * age=0.9 (age 30, 25-45) * duration=1.0 (7/7*0.85=0.85, min 1.0) = 81.00
        assertEquals(new BigDecimal("81.00"), result.calculatedPremium());
    }

    @Test
    void europeDestinationMultiplier() {
        PolicyType policy = createPolicyType(new BigDecimal("100.00"), new BigDecimal("500000"));

        InsurancePremiumEngine.PremiumResult result = engine.calculatePremium(policy, "France", 7, 30);

        assertEquals(1.0, result.destinationMultiplier(), 0.01);
    }

    @Test
    void asiaDestinationMultiplier() {
        PolicyType policy = createPolicyType(new BigDecimal("100.00"), new BigDecimal("500000"));

        InsurancePremiumEngine.PremiumResult result = engine.calculatePremium(policy, "Thailand", 7, 30);

        assertEquals(1.1, result.destinationMultiplier(), 0.01);
    }

    @Test
    void africaDestinationMultiplier() {
        PolicyType policy = createPolicyType(new BigDecimal("100.00"), new BigDecimal("500000"));

        InsurancePremiumEngine.PremiumResult result = engine.calculatePremium(policy, "Kenya", 7, 30);

        assertEquals(1.4, result.destinationMultiplier(), 0.01);
    }

    @Test
    void adventureDestinationMultiplier() {
        PolicyType policy = createPolicyType(new BigDecimal("100.00"), new BigDecimal("500000"));

        // "adventure" keyword triggers the adventure multiplier (1.8) via fallback
        InsurancePremiumEngine.PremiumResult result = engine.calculatePremium(policy, "Mountain adventure", 7, 30);

        assertEquals(1.8, result.destinationMultiplier(), 0.01);
    }

    @Test
    void unknownDestinationFallsBackTo1Point1() {
        PolicyType policy = createPolicyType(new BigDecimal("100.00"), new BigDecimal("500000"));

        InsurancePremiumEngine.PremiumResult result = engine.calculatePremium(policy, "Atlantis", 7, 30);

        assertEquals(1.1, result.destinationMultiplier(), 0.01);
    }

    @Test
    void ageUnder25HasStandardMultiplier() {
        PolicyType policy = createPolicyType(new BigDecimal("100.00"), new BigDecimal("500000"));

        InsurancePremiumEngine.PremiumResult result = engine.calculatePremium(policy, "Domestic", 7, 20);

        assertEquals(1.0, result.ageMultiplier(), 0.01);
    }

    @Test
    void age25to45HasDiscountedMultiplier() {
        PolicyType policy = createPolicyType(new BigDecimal("100.00"), new BigDecimal("500000"));

        InsurancePremiumEngine.PremiumResult result = engine.calculatePremium(policy, "Domestic", 7, 35);

        assertEquals(0.9, result.ageMultiplier(), 0.01);
    }

    @Test
    void age46to60HasHigherMultiplier() {
        PolicyType policy = createPolicyType(new BigDecimal("100.00"), new BigDecimal("500000"));

        InsurancePremiumEngine.PremiumResult result = engine.calculatePremium(policy, "Domestic", 7, 55);

        assertEquals(1.1, result.ageMultiplier(), 0.01);
    }

    @Test
    void ageOver60HasHighestMultiplier() {
        PolicyType policy = createPolicyType(new BigDecimal("100.00"), new BigDecimal("500000"));

        InsurancePremiumEngine.PremiumResult result = engine.calculatePremium(policy, "Domestic", 7, 65);

        assertEquals(1.4, result.ageMultiplier(), 0.01);
    }

    @Test
    void durationMultiplierIncreasesWithDays() {
        PolicyType policy = createPolicyType(new BigDecimal("100.00"), new BigDecimal("500000"));

        InsurancePremiumEngine.PremiumResult shortTrip = engine.calculatePremium(policy, "Domestic", 3, 30);
        InsurancePremiumEngine.PremiumResult longTrip = engine.calculatePremium(policy, "Domestic", 14, 30);

        assertTrue(longTrip.durationMultiplier() > shortTrip.durationMultiplier());
    }

    @Test
    void durationMultiplierMinimumIsOne() {
        PolicyType policy = createPolicyType(new BigDecimal("100.00"), new BigDecimal("500000"));

        InsurancePremiumEngine.PremiumResult result = engine.calculatePremium(policy, "Domestic", 1, 30);

        assertTrue(result.durationMultiplier() >= 1.0);
    }

    @Test
    void premiumCalculationCombinesAllMultipliers() {
        PolicyType policy = createPolicyType(new BigDecimal("200.00"), new BigDecimal("1000000"));

        InsurancePremiumEngine.PremiumResult result = engine.calculatePremium(policy, "Kenya", 14, 55);

        // base=200, dest=1.4, age=1.1, duration=14/7*0.85=1.7
        BigDecimal expected = new BigDecimal("200.00")
                .multiply(BigDecimal.valueOf(1.4))
                .multiply(BigDecimal.valueOf(1.1))
                .multiply(BigDecimal.valueOf(1.7))
                .setScale(2, java.math.RoundingMode.HALF_UP);

        assertEquals(expected, result.calculatedPremium());
    }

    @Test
    void zeroBasePremiumReturnsZero() {
        PolicyType policy = createPolicyType(BigDecimal.ZERO, new BigDecimal("500000"));

        InsurancePremiumEngine.PremiumResult result = engine.calculatePremium(policy, "Domestic", 7, 30);

        assertEquals(BigDecimal.ZERO.setScale(2), result.calculatedPremium());
    }
}

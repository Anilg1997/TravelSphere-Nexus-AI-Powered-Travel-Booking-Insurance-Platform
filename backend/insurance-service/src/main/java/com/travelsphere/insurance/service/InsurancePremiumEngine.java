package com.travelsphere.insurance.service;

import com.travelsphere.insurance.model.PolicyType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Component
public class InsurancePremiumEngine {

    private static final Map<String, Double> DESTINATION_RISK_MULTIPLIERS = Map.of(
        "Europe", 1.0, "Asia", 1.1, "Americas", 1.2,
        "Africa", 1.4, "Australia", 1.0, "Middle East", 1.2,
        "Adventure", 1.8, "Domestic", 0.9
    );

    private static final Map<String, Double> AGE_MULTIPLIERS = Map.of(
        "under25", 1.0, "25-45", 0.9, "46-60", 1.1, "over60", 1.4
    );

    public PremiumResult calculatePremium(PolicyType policyType, String destination,
                                          int durationDays, int travellerAge) {
        BigDecimal baseRate = policyType.getBasePremium();

        double destinationMultiplier = getDestinationMultiplier(destination);
        double ageMultiplier = getAgeMultiplier(travellerAge);
        double durationMultiplier = Math.max(1.0, (durationDays / 7.0) * 0.85);

        BigDecimal calculatedPremium = baseRate
                .multiply(BigDecimal.valueOf(destinationMultiplier))
                .multiply(BigDecimal.valueOf(ageMultiplier))
                .multiply(BigDecimal.valueOf(durationMultiplier))
                .setScale(2, RoundingMode.HALF_UP);

        return new PremiumResult(calculatedPremium, destinationMultiplier,
                                 ageMultiplier, durationMultiplier);
    }

    private static final Map<String, List<String>> DESTINATION_COUNTRIES = Map.of(
        "Europe", List.of("france", "uk", "germany", "italy", "spain", "switzerland", "austria",
                          "netherlands", "belgium", "sweden", "norway", "denmark", "greece", "portugal"),
        "Asia", List.of("india", "thailand", "japan", "china", "singapore", "malaysia", "vietnam",
                         "indonesia", "sri lanka", "nepal", "uae", "dubai", "turkey", "maldives"),
        "Americas", List.of("usa", "canada", "brazil", "mexico", "argentina", "peru", "chile", "colombia"),
        "Africa", List.of("kenya", "tanzania", "south africa", "egypt", "morocco", "ghana", "nigeria"),
        "Australia", List.of("australia", "new zealand", "fiji"),
        "Middle East", List.of("qatar", "oman", "kuwait", "bahrain", "saudi arabia", "jordan", "israel")
    );

    private double getDestinationMultiplier(String destination) {
        String dest = destination.toLowerCase().trim();
        // Check by country name first (sorted by length descending to avoid substring matches)
        for (var entry : DESTINATION_COUNTRIES.entrySet()) {
            boolean matched = entry.getValue().stream()
                    .sorted((a, b) -> Integer.compare(b.length(), a.length()))
                    .anyMatch(country -> dest.contains(country));
            if (matched) {
                return DESTINATION_RISK_MULTIPLIERS.get(entry.getKey());
            }
        }
        // Fallback to region name check
        for (var entry : DESTINATION_RISK_MULTIPLIERS.entrySet()) {
            if (dest.contains(entry.getKey().toLowerCase())) {
                return entry.getValue();
            }
        }
        if (dest.contains("adventure") || dest.contains("trek") || dest.contains("ski")) {
            return DESTINATION_RISK_MULTIPLIERS.get("Adventure");
        }
        return 1.1;
    }

    private double getAgeMultiplier(int age) {
        if (age < 25) return AGE_MULTIPLIERS.get("under25");
        if (age <= 45) return AGE_MULTIPLIERS.get("25-45");
        if (age <= 60) return AGE_MULTIPLIERS.get("46-60");
        return AGE_MULTIPLIERS.get("over60");
    }

    public record PremiumResult(BigDecimal calculatedPremium, double destinationMultiplier,
                                double ageMultiplier, double durationMultiplier) {}
}

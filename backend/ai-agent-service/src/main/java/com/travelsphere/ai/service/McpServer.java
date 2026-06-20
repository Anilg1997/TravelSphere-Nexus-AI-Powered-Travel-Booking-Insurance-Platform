package com.travelsphere.ai.service;

import com.travelsphere.ai.dto.McpToolRequest;
import com.travelsphere.ai.dto.McpToolResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Service
@Slf4j
public class McpServer {

    private final Map<String, ToolDefinition> tools = new ConcurrentHashMap<>();
    private final Map<String, Long> toolCallHistory = new ConcurrentHashMap<>();

    public record ToolDefinition(
            String name,
            String description,
            Map<String, Object> inputSchema,
            Function<Map<String, Object>, Map<String, Object>> handler
    ) {}

    @PostConstruct
    public void init() {
        registerFlightTools();
        registerHotelTools();
        registerBookingTools();
        registerInsuranceTools();
        registerGeneralTools();
        log.info("MCP Server initialized with {} tools", tools.size());
    }

    private void registerFlightTools() {
        registerTool("search_flights", "Search for available flights based on origin, destination, and date",
                Map.of("from", "string", "to", "string", "date", "string", "passengers", "integer"),
                params -> Map.of(
                        "flights", List.of(
                                Map.of("flight", "AI-101", "from", params.getOrDefault("from", "BOM"),
                                        "to", params.getOrDefault("to", "DEL"), "price", 4500,
                                        "departure", "06:00", "arrival", "08:00", "seats", 42),
                                Map.of("flight", "AI-202", "from", params.getOrDefault("from", "BOM"),
                                        "to", params.getOrDefault("to", "DEL"), "price", 5200,
                                        "departure", "14:00", "arrival", "16:00", "seats", 28)
                        ),
                        "total_results", 2
                ));

        registerTool("get_flight_status", "Get real-time status of a flight by number",
                Map.of("flight_number", "string", "date", "string"),
                params -> Map.of(
                        "flight_number", params.get("flight_number"),
                        "status", "ON_TIME",
                        "departure_gate", "A12",
                        "arrival_gate", "B7",
                        "departure_time", "06:00",
                        "arrival_time", "08:00"
                ));
    }

    private void registerHotelTools() {
        registerTool("search_hotels", "Search for hotels in a city with filters",
                Map.of("city", "string", "check_in", "string", "check_out", "string",
                        "guests", "integer", "min_stars", "integer"),
                params -> Map.of(
                        "hotels", List.of(
                                Map.of("name", "Grand Palace Hotel", "city", params.getOrDefault("city", "Mumbai"),
                                        "stars", 5, "price_per_night", 8500, "rating", 4.5, "available", 10),
                                Map.of("name", "Seaside Resort", "city", params.getOrDefault("city", "Mumbai"),
                                        "stars", 4, "price_per_night", 4200, "rating", 4.2, "available", 25)
                        ),
                        "total_results", 2
                ));
    }

    private void registerBookingTools() {
        registerTool("create_booking", "Create a new booking for flights, hotels, or packages",
                Map.of("service_type", "string", "service_id", "string",
                        "user_id", "string", "travel_date", "string", "quantity", "integer"),
                params -> Map.of(
                        "booking_ref", "BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                        "status", "CONFIRMED",
                        "service_type", params.get("service_type"),
                        "total_amount", 12500,
                        "message", "Booking confirmed successfully"
                ));

        registerTool("cancel_booking", "Cancel an existing booking by reference",
                Map.of("booking_ref", "string", "reason", "string"),
                params -> Map.of(
                        "booking_ref", params.get("booking_ref"),
                        "status", "CANCELLED",
                        "refund_amount", 11250,
                        "message", "Booking cancelled successfully. 90% refund processed."
                ));

        registerTool("check_booking_status", "Check the status of a booking",
                Map.of("booking_ref", "string"),
                params -> Map.of(
                        "booking_ref", params.get("booking_ref"),
                        "status", "CONFIRMED",
                        "payment_status", "PAID",
                        "travel_date", "2026-07-15",
                        "passenger_name", "John Doe"
                ));
    }

    private void registerInsuranceTools() {
        registerTool("get_insurance_plans", "Get available travel insurance plans",
                Map.of("destination", "string", "duration_days", "integer", "traveler_age", "integer"),
                params -> Map.of(
                        "plans", List.of(
                                Map.of("name", "Basic Shield", "coverage", 500000,
                                        "premium", 499, "duration", params.getOrDefault("duration_days", 7)),
                                Map.of("name", "Standard Shield", "coverage", 2000000,
                                        "premium", 1299, "duration", params.getOrDefault("duration_days", 7)),
                                Map.of("name", "Premium Shield", "coverage", 5000000,
                                        "premium", 2499, "duration", params.getOrDefault("duration_days", 7))
                        )
                ));

        registerTool("file_insurance_claim", "File an insurance claim",
                Map.of("policy_id", "string", "claim_type", "string",
                        "description", "string", "amount", "number"),
                params -> Map.of(
                        "claim_id", "CLM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                        "status", "FILED",
                        "estimated_settlement_date", "2026-07-30",
                        "message", "Claim filed successfully. We will review within 5 business days."
                ));
    }

    private void registerGeneralTools() {
        registerTool("get_destination_info", "Get information about a travel destination",
                Map.of("destination", "string"),
                params -> {
                    String dest = (String) params.getOrDefault("destination", "Unknown");
                    return Map.of(
                            "destination", dest,
                            "country", "India",
                            "best_time_to_visit", "October to March",
                            "currency", "INR",
                            "language", "Hindi, English",
                            "timezone", "IST (UTC+5:30)",
                            "popular_attractions", List.of(
                                    "Historical monuments", "Local cuisine", "Shopping districts"
                            ),
                            "travel_tips", List.of(
                                    "Book accommodations in advance",
                                    "Carry local currency",
                                    "Get travel insurance"
                            )
                    );
                });

        registerTool("get_weather", "Get weather forecast for a destination",
                Map.of("destination", "string", "date", "string"),
                params -> Map.of(
                        "destination", params.get("destination"),
                        "temperature", "32°C",
                        "condition", "Sunny",
                        "humidity", "65%",
                        "wind_speed", "12 km/h",
                        "forecast", List.of(
                                Map.of("day", "Today", "temp", "32°C", "condition", "Sunny"),
                                Map.of("day", "Tomorrow", "temp", "30°C", "condition", "Partly Cloudy")
                        )
                ));

        registerTool("currency_converter", "Convert currency between different currencies",
                Map.of("from_currency", "string", "to_currency", "string", "amount", "number"),
                params -> Map.of(
                        "from", params.getOrDefault("from_currency", "USD"),
                        "to", params.getOrDefault("to_currency", "INR"),
                        "amount", params.getOrDefault("amount", 100),
                        "converted_amount", 8500,
                        "exchange_rate", 85.0,
                        "last_updated", new Date().toString()
                ));

        registerTool("get_travel_advisory", "Get travel advisories and safety information",
                Map.of("destination", "string"),
                params -> Map.of(
                        "destination", params.get("destination"),
                        "advisory_level", "LEVEL_1 - Exercise Normal Precautions",
                        "safety_rating", "Safe for tourists",
                        "health_advisory", "No special vaccinations required",
                        "local_emergency_numbers", Map.of(
                                "police", "100", "ambulance", "102", "fire", "101"
                        )
                ));
    }

    public void registerTool(String name, String description, Map<String, Object> inputSchema,
                             Function<Map<String, Object>, Map<String, Object>> handler) {
        tools.put(name, new ToolDefinition(name, description, inputSchema, handler));
    }

    public McpToolResponse executeTool(McpToolRequest request) {
        long start = System.currentTimeMillis();
        ToolDefinition tool = tools.get(request.getToolName());
        if (tool == null) {
            return McpToolResponse.builder()
                    .success(false).toolName(request.getToolName())
                    .error("Tool not found: " + request.getToolName())
                    .executionTimeMs(System.currentTimeMillis() - start).build();
        }
        try {
            Map<String, Object> result = tool.handler().apply(
                    request.getParameters() != null ? request.getParameters() : Map.of());
            toolCallHistory.merge(request.getToolName(), 1L, Long::sum);
            return McpToolResponse.builder()
                    .success(true).toolName(request.getToolName())
                    .result(result)
                    .executionTimeMs(System.currentTimeMillis() - start).build();
        } catch (Exception e) {
            log.error("MCP tool execution error: {}", e.getMessage());
            return McpToolResponse.builder()
                    .success(false).toolName(request.getToolName())
                    .error("Execution error: " + e.getMessage())
                    .executionTimeMs(System.currentTimeMillis() - start).build();
        }
    }

    public List<ToolDefinition> listTools() {
        return List.copyOf(tools.values());
    }

    public Map<String, Long> getToolCallStatistics() {
        return Map.copyOf(toolCallHistory);
    }
}

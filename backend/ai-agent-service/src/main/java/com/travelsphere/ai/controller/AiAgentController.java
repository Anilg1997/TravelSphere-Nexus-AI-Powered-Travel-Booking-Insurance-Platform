package com.travelsphere.ai.controller;

import com.travelsphere.ai.dto.*;
import com.travelsphere.ai.service.AiAgentService;
import com.travelsphere.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@Tag(name = "AI Agent", description = "AI-powered chat, trip planning, and recommendations")
public class AiAgentController {

    private final AiAgentService aiAgentService;

    @PostMapping("/chat")
    @Operation(summary = "Chat with AI assistant (SSE streaming)")
    public ResponseEntity<ApiResponse<ChatResponse>> chat(@Valid @RequestBody ChatRequest request) {
        ChatResponse response = aiAgentService.chat(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/plan-trip")
    @Operation(summary = "AI-powered trip planning")
    public ResponseEntity<ApiResponse<TripPlanResponse>> planTrip(@Valid @RequestBody TripPlanRequest request) {
        TripPlanResponse response = aiAgentService.planTrip(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Trip planned successfully"));
    }

    @GetMapping("/recommendations")
    @Operation(summary = "Get personalized travel recommendations")
    public ResponseEntity<ApiResponse<String>> getRecommendations(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        String recommendations = aiAgentService.getRecommendations(userId);
        return ResponseEntity.ok(ApiResponse.success(recommendations));
    }

    @PostMapping("/insurance-advisor")
    @Operation(summary = "AI insurance advisor")
    public ResponseEntity<ApiResponse<String>> getInsuranceAdvisor(
            @RequestParam String destination,
            @RequestParam int duration,
            @RequestParam int age) {
        String advice = aiAgentService.getInsuranceAdvisor(destination, duration, age);
        return ResponseEntity.ok(ApiResponse.success(advice));
    }
}

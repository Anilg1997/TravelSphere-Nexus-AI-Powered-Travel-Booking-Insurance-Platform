package com.travelsphere.ai.service;

import com.travelsphere.ai.dto.*;

public interface AiAgentService {
    ChatResponse chat(ChatRequest request);
    TripPlanResponse planTrip(TripPlanRequest request);
    String getRecommendations(String userId);
    String getInsuranceAdvisor(String destination, int duration, int age);
}

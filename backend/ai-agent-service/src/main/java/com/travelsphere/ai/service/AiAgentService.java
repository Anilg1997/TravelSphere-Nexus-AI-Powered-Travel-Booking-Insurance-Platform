package com.travelsphere.ai.service;

import com.travelsphere.ai.dto.*;
import com.travelsphere.admin.dto.SupportTicketRequest;

import java.util.List;
import java.util.Map;

public interface AiAgentService {
    ChatResponse chat(ChatRequest request);
    TripPlanResponse planTrip(TripPlanRequest request);
    String getRecommendations(String userId);
    String getInsuranceAdvisor(String destination, int duration, int age);

    McpToolResponse executeTool(McpToolRequest request);
    List<Map<String, Object>> listTools();

    IngestionResponse ingestDocument(IngestionRequest request);
    IngestionResponse ingestBatch(List<IngestionRequest> requests);

    SemanticSearchResponse semanticSearch(SemanticSearchRequest request);

    ChatResponse ragChat(ChatRequest request);

    Map<String, Long> getToolStatistics();
}

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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@Tag(name = "AI Agent", description = "AI-powered chat, RAG, MCP tools, trip planning, document ingestion, and semantic search")
public class AiAgentController {

    private final AiAgentService aiAgentService;

    @PostMapping("/chat")
    @Operation(summary = "Chat with AI assistant")
    public ResponseEntity<ApiResponse<ChatResponse>> chat(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(ApiResponse.success(aiAgentService.chat(request)));
    }

    @PostMapping("/rag-chat")
    @Operation(summary = "Chat with AI using RAG context from vector database")
    public ResponseEntity<ApiResponse<ChatResponse>> ragChat(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(ApiResponse.success(aiAgentService.ragChat(request)));
    }

    @PostMapping("/plan-trip")
    @Operation(summary = "AI-powered trip planning with RAG")
    public ResponseEntity<ApiResponse<TripPlanResponse>> planTrip(@Valid @RequestBody TripPlanRequest request) {
        return ResponseEntity.ok(ApiResponse.success(aiAgentService.planTrip(request)));
    }

    @GetMapping("/recommendations")
    @Operation(summary = "Get personalized travel recommendations")
    public ResponseEntity<ApiResponse<String>> getRecommendations(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        return ResponseEntity.ok(ApiResponse.success(aiAgentService.getRecommendations(userId)));
    }

    @PostMapping("/insurance-advisor")
    @Operation(summary = "AI insurance advisor with RAG context")
    public ResponseEntity<ApiResponse<String>> getInsuranceAdvisor(
            @RequestParam String destination, @RequestParam int duration, @RequestParam int age) {
        return ResponseEntity.ok(ApiResponse.success(aiAgentService.getInsuranceAdvisor(destination, duration, age)));
    }

    @PostMapping("/mcp/execute")
    @Operation(summary = "Execute an MCP tool")
    public ResponseEntity<ApiResponse<McpToolResponse>> executeTool(@Valid @RequestBody McpToolRequest request) {
        return ResponseEntity.ok(ApiResponse.success(aiAgentService.executeTool(request)));
    }

    @GetMapping("/mcp/tools")
    @Operation(summary = "List all available MCP tools")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> listTools() {
        return ResponseEntity.ok(ApiResponse.success(aiAgentService.listTools()));
    }

    @GetMapping("/mcp/stats")
    @Operation(summary = "Get MCP tool call statistics")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getToolStats() {
        return ResponseEntity.ok(ApiResponse.success(aiAgentService.getToolStatistics()));
    }

    @PostMapping("/ingest")
    @Operation(summary = "Ingest a document into the vector knowledge base")
    public ResponseEntity<ApiResponse<IngestionResponse>> ingestDocument(
            @Valid @RequestBody IngestionRequest request) {
        return ResponseEntity.ok(ApiResponse.success(aiAgentService.ingestDocument(request)));
    }

    @PostMapping("/ingest/batch")
    @Operation(summary = "Batch ingest multiple documents")
    public ResponseEntity<ApiResponse<IngestionResponse>> ingestBatch(
            @Valid @RequestBody List<IngestionRequest> requests) {
        return ResponseEntity.ok(ApiResponse.success(aiAgentService.ingestBatch(requests)));
    }

    @PostMapping("/semantic-search")
    @Operation(summary = "Semantic search across travel knowledge base")
    public ResponseEntity<ApiResponse<SemanticSearchResponse>> semanticSearch(
            @Valid @RequestBody SemanticSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(aiAgentService.semanticSearch(request)));
    }
}

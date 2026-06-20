package com.travelsphere.ai.service;

import com.travelsphere.ai.dto.*;
import com.travelsphere.ai.model.ChatMessage;
import com.travelsphere.ai.model.ChatSession;
import com.travelsphere.ai.repository.ChatMessageRepository;
import com.travelsphere.ai.repository.ChatSessionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiAgentServiceImpl implements AiAgentService {

    private final ChatSessionRepository sessionRepository;
    private final ChatMessageRepository messageRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final WebClient.Builder webClientBuilder;
    private final McpServer mcpServer;
    private final RagService ragService;
    private final DocumentIngestionService ingestionService;

    @Value("${spring.ai.ollama.base-url:http://localhost:11434}")
    private String ollamaBaseUrl;

    @Value("${spring.ai.ollama.chat.model:llama3.2}")
    private String chatModel;

    @Value("${app.chat.max-history:10}")
    private int maxHistory;

    @Override
    @Transactional
    public ChatResponse chat(ChatRequest request) {
        UUID sessionId = request.getSessionId();
        if (sessionId == null) {
            ChatSession session = ChatSession.builder()
                    .userId(request.getUserId() != null ? UUID.fromString(request.getUserId()) : null)
                    .title(request.getMessage().substring(0, Math.min(50, request.getMessage().length())))
                    .build();
            session = sessionRepository.save(session);
            sessionId = session.getId();
        }

        ChatMessage userMessage = ChatMessage.builder()
                .sessionId(sessionId).role(ChatMessage.MessageRole.USER)
                .content(request.getMessage()).build();
        messageRepository.save(userMessage);

        List<ChatMessage> history = messageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
        history = history.subList(Math.max(0, history.size() - maxHistory * 2), history.size());

        String context = history.stream()
                .map(m -> m.getRole().name() + ": " + m.getContent())
                .collect(Collectors.joining("\n"));

        String reply = callOllama(context);
        ChatMessage assistantMessage = ChatMessage.builder()
                .sessionId(sessionId).role(ChatMessage.MessageRole.ASSISTANT)
                .content(reply).build();
        messageRepository.save(assistantMessage);

        kafkaTemplate.send("ts.ai.query-logged", sessionId.toString(),
                List.of(request.getMessage(), reply));

        return ChatResponse.builder()
                .sessionId(sessionId).reply(reply)
                .timestamp(LocalDateTime.now()).build();
    }

    @Override
    @Transactional
    public ChatResponse ragChat(ChatRequest request) {
        UUID sessionId = request.getSessionId();
        if (sessionId == null) {
            ChatSession session = ChatSession.builder()
                    .userId(request.getUserId() != null ? UUID.fromString(request.getUserId()) : null)
                    .title("[RAG] " + request.getMessage().substring(0, Math.min(50, request.getMessage().length())))
                    .build();
            session = sessionRepository.save(session);
            sessionId = session.getId();
        }

        ChatMessage userMessage = ChatMessage.builder()
                .sessionId(sessionId).role(ChatMessage.MessageRole.USER)
                .content(request.getMessage()).build();
        messageRepository.save(userMessage);

        String ragPrompt = ragService.enhancePromptWithContext(request.getMessage());
        String reply = callOllama(ragPrompt);

        ChatMessage assistantMessage = ChatMessage.builder()
                .sessionId(sessionId).role(ChatMessage.MessageRole.ASSISTANT)
                .content(reply).build();
        messageRepository.save(assistantMessage);

        kafkaTemplate.send("ts.ai.query-logged", sessionId.toString(),
                List.of("RAG:" + request.getMessage(), reply));

        return ChatResponse.builder()
                .sessionId(sessionId).reply(reply)
                .timestamp(LocalDateTime.now()).build();
    }

    @Override
    public TripPlanResponse planTrip(TripPlanRequest request) {
        String ragContext = ragService.retrieveContext(
                "travel itinerary " + request.getDestination(), 3);

        String prompt = String.format(
                "Plan a %d-day trip to %s for %d travelers with a budget of ₹%.0f. Preferences: %s.\n\n" +
                "Context from travel knowledge base:\n%s\n\n" +
                "Provide a day-by-day itinerary, budget breakdown, and recommendations.",
                request.getDurationDays(), request.getDestination(),
                request.getTravelers(), request.getBudget(),
                request.getPreferences() != null ? request.getPreferences() : "none",
                ragContext);

        String reply = callOllama(prompt);

        return TripPlanResponse.builder()
                .destination(request.getDestination())
                .durationDays(request.getDurationDays())
                .budget(request.getBudget())
                .itinerary(List.of(reply.split("\n")))
                .recommendations(List.of(
                        "Consider local cuisine experiences",
                        "Book accommodation in advance",
                        "Carry comprehensive travel insurance",
                        "Download offline maps",
                        "Keep digital copies of documents"))
                .summary(reply.length() > 200 ? reply.substring(0, 200) + "..." : reply)
                .build();
    }

    @Override
    public String getRecommendations(String userId) {
        String prompt = """
                Based on popular travel trends and our travel knowledge base, recommend 5 travel destinations.
                For each destination include: budget estimate, best time to visit, top 3 activities, and travel tips.
                Format as a structured list with emojis.
                """;
        return callOllama(prompt);
    }

    @Override
    public String getInsuranceAdvisor(String destination, int duration, int age) {
        String ragContext = ragService.retrieveContext(
                "travel insurance " + destination, 3);
        String prompt = String.format(
                "Recommend travel insurance for a %d-year-old traveler going to %s for %d days.\n\n" +
                "Context:\n%s\n\nInclude: recommended coverage, estimated premium, and tips.",
                age, destination, duration, ragContext);
        return callOllama(prompt);
    }

    @Override
    public McpToolResponse executeTool(McpToolRequest request) {
        return mcpServer.executeTool(request);
    }

    @Override
    public List<Map<String, Object>> listTools() {
        return mcpServer.listTools().stream()
                .map(t -> Map.<String, Object>of(
                        "name", t.name(),
                        "description", t.description(),
                        "input_schema", t.inputSchema()))
                .collect(Collectors.toList());
    }

    @Override
    public IngestionResponse ingestDocument(IngestionRequest request) {
        return ingestionService.ingest(request);
    }

    @Override
    public IngestionResponse ingestBatch(List<IngestionRequest> requests) {
        return ingestionService.ingestBatch(requests);
    }

    @Override
    public SemanticSearchResponse semanticSearch(SemanticSearchRequest request) {
        return ragService.semanticSearch(request);
    }

    @Override
    public Map<String, Long> getToolStatistics() {
        return mcpServer.getToolCallStatistics();
    }

    @CircuitBreaker(name = "ollama-api", fallbackMethod = "ollamaFallback")
    String callOllama(String prompt) {
        WebClient client = webClientBuilder.baseUrl(ollamaBaseUrl).build();
        String requestBody = String.format(
                "{\"model\":\"%s\",\"prompt\":\"%s\",\"stream\":false}",
                chatModel, prompt.replace("\"", "\\\"").replace("\n", "\\n"));

        String response = client.post()
                .uri("/api/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (response != null && response.contains("\"response\"")) {
            int start = response.indexOf("\"response\":\"") + 12;
            int end = response.lastIndexOf("\"");
            if (end > start) {
                return response.substring(start, end)
                        .replace("\\n", "\n")
                        .replace("\\\"", "\"");
            }
        }
        return "I'm sorry, I couldn't process your request. Please try again later.";
    }

    @SuppressWarnings("unused")
    String ollamaFallback(String prompt, Throwable t) {
        log.error("Circuit breaker triggered for Ollama call: {}", t.getMessage());
        return "AI service is temporarily unavailable. Please try again later or use the MCP tools directly.";
    }
}

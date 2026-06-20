package com.travelsphere.ai.service;

import com.travelsphere.ai.dto.SemanticSearchRequest;
import com.travelsphere.ai.dto.SemanticSearchResponse;
import com.travelsphere.ai.dto.ChatRequest;
import com.travelsphere.ai.dto.ChatResponse;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RagService {

    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingModel embeddingModel;

    @Value("${app.rag.top-k:5}")
    private int topK;

    @Value("${app.rag.min-score:0.7}")
    private double minScore;

    public SemanticSearchResponse semanticSearch(SemanticSearchRequest request) {
        Embedding queryEmbedding = embeddingModel.embed(request.getQuery()).content();
        List<EmbeddingMatch<TextSegment>> matches = embeddingStore.findRelevant(
                queryEmbedding, request.getTopK() > 0 ? request.getTopK() : topK);

        List<SemanticSearchResponse.SearchResult> results = matches.stream()
                .filter(m -> m.score() >= (request.getMinScore() != null ? request.getMinScore() : minScore))
                .map(m -> SemanticSearchResponse.SearchResult.builder()
                        .chunkId(m.embeddingId())
                        .content(m.embedded().text())
                        .score(m.score())
                        .build())
                .collect(Collectors.toList());

        return SemanticSearchResponse.builder()
                .query(request.getQuery())
                .results(results)
                .totalResults(results.size())
                .build();
    }

    public String retrieveContext(String query, int topK) {
        Embedding queryEmbedding = embeddingModel.embed(query).content();
        List<EmbeddingMatch<TextSegment>> matches = embeddingStore.findRelevant(
                queryEmbedding, topK > 0 ? topK : this.topK);

        return matches.stream()
                .filter(m -> m.score() >= minScore)
                .map(m -> m.embedded().text())
                .collect(Collectors.joining("\n\n---\n\n"));
    }

    public String enhancePromptWithContext(String userMessage) {
        String context = retrieveContext(userMessage, topK);
        if (context.isBlank()) {
            return userMessage;
        }
        return """
               Use the following travel knowledge context to answer the user's question.
               If the context is not relevant, answer based on your general knowledge.
               
               Context:
               %s
               
               User Question: %s
               """.formatted(context, userMessage);
    }
}

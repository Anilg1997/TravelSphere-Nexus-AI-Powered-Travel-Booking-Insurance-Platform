package com.travelsphere.ai.service;

import com.travelsphere.ai.dto.IngestionRequest;
import com.travelsphere.ai.dto.IngestionResponse;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentIngestionService {

    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingModel embeddingModel;

    @Value("${app.rag.chunk-size:500}")
    private int chunkSize;

    @Value("${app.rag.chunk-overlap:50}")
    private int chunkOverlap;

    public IngestionResponse ingest(IngestionRequest request) {
        Document doc = Document.from(request.getContent());
        var splitter = DocumentSplitters.recursive(chunkSize, chunkOverlap);
        List<TextSegment> segments = splitter.split(doc);

        List<String> chunkIds = new ArrayList<>();
        int batchSize = 20;

        for (int i = 0; i < segments.size(); i += batchSize) {
            List<TextSegment> batch = segments.subList(i, Math.min(i + batchSize, segments.size()));
            List<Embedding> embeddings = embeddingModel.embedAll(batch).content();
            List<String> ids = embeddingStore.addAll(embeddings, batch);
            chunkIds.addAll(ids);
        }

        log.info("Ingested {} chunks from source: {}", segments.size(), request.getSource());
        return IngestionResponse.builder()
                .source(request.getSource())
                .chunksCreated(segments.size())
                .success(true)
                .message("Successfully ingested " + segments.size() + " chunks into vector store")
                .chunkIds(chunkIds)
                .build();
    }

    public IngestionResponse ingestBatch(List<IngestionRequest> requests) {
        int totalChunks = 0;
        List<String> allIds = new ArrayList<>();
        for (IngestionRequest req : requests) {
            IngestionResponse resp = ingest(req);
            totalChunks += resp.getChunksCreated();
            allIds.addAll(resp.getChunkIds());
        }
        return IngestionResponse.builder()
                .source("batch")
                .chunksCreated(totalChunks)
                .success(true)
                .message("Successfully ingested " + totalChunks + " chunks from " + requests.size() + " documents")
                .chunkIds(allIds)
                .build();
    }
}

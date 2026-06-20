package com.travelsphere.ai.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import io.qdrant.client.grpc.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutionException;

@Configuration
@Slf4j
public class QdrantConfig {

    @Value("${app.qdrant.host:localhost}")
    private String qdrantHost;

    @Value("${app.qdrant.port:6333}")
    private int qdrantPort;

    @Value("${app.qdrant.collection:travelsphere_knowledge}")
    private String collectionName;

    @Value("${app.qdrant.vector-size:768}")
    private int vectorSize;

    @Value("${spring.ai.ollama.base-url:http://localhost:11434}")
    private String ollamaBaseUrl;

    @Value("${spring.ai.ollama.embedding.model:nomic-embed-text}")
    private String embeddingModel;

    @Bean
    public QdrantClient qdrantClient() {
        return new QdrantClient(QdrantGrpcClient.newBuilder(qdrantHost, qdrantPort, false).build());
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore(QdrantClient qdrantClient) {
        try {
            var collections = qdrantClient.listCollectionsAsync().get();
            boolean exists = collections.stream()
                    .anyMatch(c -> c.getName().equals(collectionName));
            if (!exists) {
                qdrantClient.createCollectionAsync(Collections.CreateCollection.newBuilder()
                        .setCollectionName(collectionName)
                        .setVectorsConfig(Collections.VectorsConfig.newBuilder()
                                .setParams(Collections.VectorParams.newBuilder()
                                        .setSize(vectorSize)
                                        .setDistance(Collections.Distance.Cosine)
                                        .build())
                                .build())
                        .build()).get();
                log.info("Created Qdrant collection: {}", collectionName);
            }
        } catch (InterruptedException | ExecutionException e) {
            log.warn("Could not create Qdrant collection (may already exist): {}", e.getMessage());
        }
        return QdrantEmbeddingStore.builder()
                .host(qdrantHost)
                .port(qdrantPort)
                .collectionName(collectionName)
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return OllamaEmbeddingModel.builder()
                .baseUrl(ollamaBaseUrl)
                .modelName(embeddingModel)
                .build();
    }
}

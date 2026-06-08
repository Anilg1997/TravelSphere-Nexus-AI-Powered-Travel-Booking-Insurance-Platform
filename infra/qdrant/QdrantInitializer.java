package com.travelsphere.infra.qdrant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class QdrantInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(QdrantInitializer.class);

    private final String qdrantHost;
    private final int qdrantPort;
    private final String collectionName;
    private final int vectorSize;
    private final RestTemplate restTemplate;
    private final ResourceLoader resourceLoader;

    public QdrantInitializer(
            @Value("${app.qdrant.host:localhost}") String host,
            @Value("${app.qdrant.port:6333}") int port,
            @Value("${app.qdrant.collection:travelsphere_knowledge}") String collection,
            @Value("${app.qdrant.vector-size:768}") int vectorSize,
            ResourceLoader resourceLoader) {
        this.qdrantHost = host;
        this.qdrantPort = port;
        this.collectionName = collection;
        this.vectorSize = vectorSize;
        this.restTemplate = new RestTemplate();
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            String baseUrl = "http://" + qdrantHost + ":" + qdrantPort;
            String collectionsUrl = baseUrl + "/collections";

            // Check if collection exists
            try {
                restTemplate.getForEntity(collectionsUrl + "/" + collectionName, String.class);
                log.info("Qdrant collection '{}' already exists", collectionName);
            } catch (Exception e) {
                // Create collection
                Map<String, Object> request = Map.of(
                        "name", collectionName,
                        "vectors", Map.of("size", vectorSize, "distance", "Cosine")
                );
                restTemplate.put(collectionsUrl + "/" + collectionName, request);
                log.info("Created Qdrant collection '{}' with vector size {}", collectionName, vectorSize);
            }
        } catch (Exception e) {
            log.warn("Failed to initialize Qdrant: {}. Will retry later.", e.getMessage());
        }
    }
}

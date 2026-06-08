package com.travelsphere.search.service;

import com.travelsphere.search.dto.*;
import com.travelsphere.search.model.SearchIndex;
import com.travelsphere.search.repository.SearchIndexRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchServiceImpl implements SearchService {

    private final SearchIndexRepository searchIndexRepository;
    private final StringRedisTemplate redisTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public SearchResponse search(SearchQueryRequest request) {
        String cacheKey = "search:" + request.getQuery() + ":" + request.getCity() + ":" + request.getEntityType();
        String cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            log.info("Cache hit for search: {}", cacheKey);
        }

        List<SearchIndex> results = searchIndexRepository.searchWithFilters(
                request.getQuery(),
                request.getEntityType(),
                request.getCity(),
                request.getCategory()
        );

        List<SearchResultItem> items = results.stream()
                .map(this::toSearchResultItem)
                .collect(Collectors.toList());

        return SearchResponse.builder()
                .results(items)
                .total(items.size())
                .page(request.getPage())
                .size(request.getSize())
                .build();
    }

    @Override
    @Transactional
    public void indexDocument(IndexDocumentRequest request) {
        SearchIndex index = SearchIndex.builder()
                .entityType(request.getEntityType())
                .entityId(request.getEntityId())
                .title(request.getTitle())
                .description(request.getDescription())
                .city(request.getCity())
                .country(request.getCountry())
                .category(request.getCategory())
                .tags(request.getTags())
                .rating(request.getRating())
                .price(request.getPrice())
                .isActive(true)
                .build();

        searchIndexRepository.save(index);

        redisTemplate.delete("search:*");

        log.info("Indexed document: {} {} - {}", request.getEntityType(), request.getEntityId(), request.getTitle());
    }

    @Override
    @Transactional
    public void removeDocument(String entityType, String entityId) {
        searchIndexRepository.findAll().stream()
                .filter(s -> s.getEntityType().equals(entityType) && s.getEntityId().toString().equals(entityId))
                .findFirst()
                .ifPresent(s -> {
                    s.setActive(false);
                    searchIndexRepository.save(s);
                });

        log.info("Removed document from index: {} {}", entityType, entityId);
    }

    @KafkaListener(topics = "ts.search.indexed", groupId = "search-service-group")
    public void onIndexEvent(String payload) {
        log.info("Received index event: {}", payload);
    }

    private SearchResultItem toSearchResultItem(SearchIndex index) {
        return SearchResultItem.builder()
                .id(index.getId())
                .entityType(index.getEntityType())
                .entityId(index.getEntityId())
                .title(index.getTitle())
                .description(index.getDescription())
                .city(index.getCity())
                .country(index.getCountry())
                .category(index.getCategory())
                .rating(index.getRating())
                .price(index.getPrice())
                .score(1.0)
                .build();
    }
}

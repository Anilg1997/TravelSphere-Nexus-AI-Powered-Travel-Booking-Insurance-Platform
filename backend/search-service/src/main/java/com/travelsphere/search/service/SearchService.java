package com.travelsphere.search.service;

import com.travelsphere.search.dto.*;

public interface SearchService {
    SearchResponse search(SearchQueryRequest request);
    void indexDocument(IndexDocumentRequest request);
    void removeDocument(String entityType, String entityId);
}

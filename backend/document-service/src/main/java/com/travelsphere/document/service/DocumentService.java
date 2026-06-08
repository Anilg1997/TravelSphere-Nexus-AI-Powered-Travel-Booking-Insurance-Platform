package com.travelsphere.document.service;

import com.travelsphere.document.dto.*;

import java.util.List;
import java.util.UUID;

public interface DocumentService {
    DocumentResponse generateDocument(GenerateDocumentRequest request, String userId);
    DocumentResponse getDocument(UUID documentId);
    List<DocumentResponse> getUserDocuments(String userId);
    byte[] downloadDocument(UUID documentId);
}

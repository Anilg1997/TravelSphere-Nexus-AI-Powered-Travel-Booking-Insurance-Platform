package com.travelsphere.document.controller;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.document.dto.*;
import com.travelsphere.document.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Tag(name = "Documents", description = "PDF generation and S3 upload/download APIs")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/generate")
    @Operation(summary = "Generate a document (booking confirmation, invoice, etc.)")
    public ResponseEntity<ApiResponse<DocumentResponse>> generateDocument(
            @Valid @RequestBody GenerateDocumentRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        DocumentResponse document = documentService.generateDocument(request, userId);
        return ResponseEntity.ok(ApiResponse.success(document, "Document generated successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get document details")
    public ResponseEntity<ApiResponse<DocumentResponse>> getDocument(@PathVariable UUID id) {
        DocumentResponse document = documentService.getDocument(id);
        return ResponseEntity.ok(ApiResponse.success(document));
    }

    @GetMapping
    @Operation(summary = "Get all user documents")
    public ResponseEntity<ApiResponse<List<DocumentResponse>>> getUserDocuments(
            @RequestHeader("X-User-Id") String userId) {
        List<DocumentResponse> documents = documentService.getUserDocuments(userId);
        return ResponseEntity.ok(ApiResponse.success(documents));
    }

    @GetMapping("/{id}/download")
    @Operation(summary = "Download document as PDF")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable UUID id) {
        byte[] content = documentService.downloadDocument(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=document.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(content);
    }
}

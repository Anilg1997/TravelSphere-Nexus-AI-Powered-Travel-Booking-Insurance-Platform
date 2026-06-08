package com.travelsphere.document.service;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.*;
import com.travelsphere.document.dto.*;
import com.travelsphere.document.model.Document;
import com.travelsphere.document.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // Feign clients for fetching booking details
    private final FlightClient flightClient;
    private final HotelClient hotelClient;
    private final TransportClient transportClient;
    private final CarRentalClient carRentalClient;
    private final PackageClient packageClient;
    private final InsuranceClient insuranceClient;

    @Override
    @Transactional
    public DocumentResponse generateDocument(GenerateDocumentRequest request, String userId) {
        // Fetch booking details via Feign client
        Map<String, Object> bookingDetails = fetchBookingDetails(request.getBookingRef(), request.getDocumentType());

        String fileName = request.getBookingRef() + "_" + request.getDocumentType() + ".pdf";
        String s3Key = "documents/" + request.getBookingRef() + "/" + fileName;

        Document document = Document.builder()
                .userId(userId != null ? UUID.fromString(userId) : null)
                .documentType(Document.DocumentType.valueOf(request.getDocumentType()))
                .fileName(fileName)
                .s3Key(s3Key)
                .contentType("application/pdf")
                .fileSize(0L)
                .bookingRef(request.getBookingRef())
                .build();

        document = documentRepository.save(document);

        kafkaTemplate.send("ts.documents.generated", document.getId().toString(), document);

        log.info("Document generated: {} for booking {} with details from {}",
                document.getDocumentType(), request.getBookingRef(), bookingDetails != null ? "Feign" : "local");

        return toDocumentResponse(document);
    }

    @Override
    public DocumentResponse getDocument(UUID documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));
        return toDocumentResponse(document);
    }

    @Override
    public List<DocumentResponse> getUserDocuments(String userId) {
        return documentRepository.findByUserIdOrderByCreatedAtDesc(UUID.fromString(userId))
                .stream()
                .map(this::toDocumentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] downloadDocument(UUID documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));

        log.info("Downloading document {} from S3: {}", documentId, document.getS3Key());
        return new byte[0];
    }

    private Map<String, Object> fetchBookingDetails(String bookingRef, String documentType) {
        try {
            ApiResponse<Map<String, Object>> response = switch (documentType.toUpperCase()) {
                case "BOOKING_CONFIRMATION", "INVOICE", "BOARDING_PASS", "TICKET" -> flightClient.getBooking(bookingRef);
                case "INSURANCE_POLICY" -> insuranceClient.getClaim(bookingRef);
                case "E_VISA" -> {
                    log.debug("E_VISA document type - no booking details to fetch");
                    yield null;
                }
                default -> {
                    log.warn("Unknown document type for booking details: {}", documentType);
                    yield null;
                }
            };
            return response != null && response.isSuccess() ? response.getData() : null;
        } catch (Exception e) {
            log.warn("Failed to fetch booking details for {}: {}", bookingRef, e.getMessage());
            return null;
        }
    }

    private DocumentResponse toDocumentResponse(Document document) {
        return DocumentResponse.builder()
                .id(document.getId())
                .documentType(document.getDocumentType().name())
                .fileName(document.getFileName())
                .s3Key(document.getS3Key())
                .contentType(document.getContentType())
                .fileSize(document.getFileSize())
                .bookingRef(document.getBookingRef())
                .createdAt(document.getCreatedAt())
                .build();
    }
}

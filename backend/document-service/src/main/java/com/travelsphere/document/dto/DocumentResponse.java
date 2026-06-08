package com.travelsphere.document.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {
    private UUID id;
    private String documentType;
    private String fileName;
    private String s3Key;
    private String contentType;
    private Long fileSize;
    private String bookingRef;
    private LocalDateTime createdAt;
}

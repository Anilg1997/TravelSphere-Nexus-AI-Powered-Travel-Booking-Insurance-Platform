package com.travelsphere.insurance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "claim_documents", schema = "insurance_schema")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ClaimDocument {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "claim_id")
    private UUID claimId;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "s3_key")
    private String s3Key;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }
}

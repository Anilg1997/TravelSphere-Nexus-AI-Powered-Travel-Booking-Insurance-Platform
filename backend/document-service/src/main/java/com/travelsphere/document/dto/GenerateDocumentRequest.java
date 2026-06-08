package com.travelsphere.document.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateDocumentRequest {
    @NotBlank
    private String documentType;

    @NotBlank
    private String bookingRef;

    private String userId;
}

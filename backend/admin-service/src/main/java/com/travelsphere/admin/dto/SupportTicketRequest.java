package com.travelsphere.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportTicketRequest {
    @NotBlank
    private String subject;

    @NotBlank
    private String description;

    private String category;
    private String priority;
    private String status;
    private String resolution;
    private String assignedTo;
}

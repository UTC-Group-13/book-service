package org.example.bookservice.dto.response;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NationalityResponse {
    private Long id;

    private String code;

    private String nameVi;

    private String nameEn;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

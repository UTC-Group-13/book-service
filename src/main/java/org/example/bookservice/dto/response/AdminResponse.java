package org.example.bookservice.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminResponse {
    private String username;

    private String fullName;

    private String email;

    private String phone;

    private String role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

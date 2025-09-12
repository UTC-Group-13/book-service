package org.example.bookservice.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AuthorRequest {
    private String fullName;
    private LocalDate birthDate;
    private String nationality;
    private String biography;
    private String email;
}
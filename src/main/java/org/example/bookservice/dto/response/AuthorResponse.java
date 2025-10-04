package org.example.bookservice.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AuthorResponse {
    private Integer id;
    private String fullName;
    private LocalDate birthDate;
    private String nationality;
    private String biography;
    private String email;
}
package org.example.bookservice.dto.request;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@Data
public class AuthorSearchRequest {

    private String fullName;

    private LocalDate birthDate;

    private String nationality;

    private String biography;

    private String email;

    private Pageable pageable;
}
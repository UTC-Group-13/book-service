package org.example.bookservice.dto.response;

import lombok.Data;

@Data
public class AuthorResponse {
    private Integer id;
    private String name; // Assuming a simple attribute 'name' exists in Author
}
package org.example.bookservice.dto.response;

import lombok.Data;

@Data
public class CategoryResponse {
    private Integer id;
    private String name; // Assuming a simple attribute 'name' exists in Category
}
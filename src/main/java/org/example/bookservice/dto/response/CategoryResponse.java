package org.example.bookservice.dto.response;

import lombok.Data;

@Data
public class CategoryResponse {
    private Integer id;
    private String name;
    private String description;
}
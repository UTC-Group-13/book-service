package org.example.bookservice.dto.response;

import lombok.Data;

@Data
public class PublisherResponse {
    private Integer id;
    private String name; // Assuming a simple attribute 'name' exists in Publisher
}
package org.example.bookservice.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class BookRequest {

    private String title;
    private String isbn;
    private Integer publishYear;
    private String language;
    private Integer quantity;
    private Integer price;
    private String description;
    private String coverImage;
    private Integer publisherId; // Assuming a many-to-one relationship, you accept the publisher ID
    private Set<Integer> categoryIds; // For many-to-many, handle IDs for simplicity
    private Set<Integer> authorIds; // For many-to-many, handle IDs for simplicity
}

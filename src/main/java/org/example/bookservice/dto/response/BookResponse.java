package org.example.bookservice.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookResponse {

    private Integer id;
    private String title;
    private String isbn;
    private Integer publishYear;
    private String language;
    private Integer quantity;
    private Integer price;
    private String description;
    private String coverImage;
    private Boolean deleteFlg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PublisherResponse publisher; // Custom DTO for publisher
    private Set<CategoryResponse> categories; // Custom DTOs for categories
    private Set<AuthorResponse> authors; // Custom DTOs for authors
}
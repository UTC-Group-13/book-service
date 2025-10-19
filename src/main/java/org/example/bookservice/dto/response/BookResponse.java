package org.example.bookservice.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
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
    private List<CategoryResponse> categories;
    private List<AuthorResponse> authors;
    private Set<Long> categoryIds; // Custom DTOs for categories
    private Set<Long> authorIds; // Custom DTOs for authors
}
package org.example.bookservice.dto.request;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookSearchRequest {

    private Integer id;
    private String title;
    private String isbn;
    private Integer publishYear;
    private String language;
    private Integer quantity;
    private Integer price;
    private String description;
    private String coverImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer publisherId;
    private Set<Integer> categoryIds;
    private Set<Integer> authorIds;
    private Pageable pageable;
}
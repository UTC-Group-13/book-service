package org.example.bookservice.dto.request;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.Set;

@Data
public class BookSearchRequest extends SearchRequest{

    private String search;
    private String title;
    private String isbn;
    private Integer publishYear;
    private String language;
    private Integer quantity;
    private Integer minPrice;
    private Integer maxPrice;
    private String description;
    private String coverImage;
    private Integer publisherId;
    private Set<Integer> categoryIds;
    private Set<Integer> authorIds;
}

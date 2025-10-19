package org.example.bookservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class BookRequest {

    @NotBlank(message = "Title không được để trống")
    private String title;

    @NotBlank(message = "ISBN không được để trống")
    private String isbn;

    @NotNull(message = "Publish year không được để trống")
    private Integer publishYear;

    @NotBlank(message = "Language không được để trống")
    private String language;

    @NotNull(message = "Quantity không được để trống")
    private Integer quantity;

    @NotNull(message = "Price không được để trống")
    private Integer price;

    @NotBlank(message = "Description không được để trống")
    private String description;

    @NotBlank(message = "Cover image không được để trống")
    private String coverImage;

    @NotNull(message = "PublisherId không được để trống")
    private Integer publisherId;

    @NotNull(message = "CategoryIds không được để trống")
    @Size(min = 1, message = "Cần ít nhất một category")
    private Set<Long> categoryIds;

    @NotNull(message = "AuthorIds không được để trống")
    @Size(min = 1, message = "Cần ít nhất một author")
    private Set<Long> authorIds;
}

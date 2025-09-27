package org.example.bookservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotNull(message = "Tên thể loại không được để trống")
    private String name;
    @NotNull(message = "Mô tả không được để trống")
    private String description;
}
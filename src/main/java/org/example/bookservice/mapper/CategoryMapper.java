package org.example.bookservice.mapper;

import org.example.bookservice.dto.request.CategoryRequest;
import org.example.bookservice.dto.response.CategoryResponse;
import org.example.bookservice.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring") // để Spring quản lý
public interface CategoryMapper {
    CategoryResponse toCategoryResponse(Category category);

    Category toCategory(CategoryRequest categoryRequest);

    List<CategoryResponse> toResponseList(List<Category> categories);
}


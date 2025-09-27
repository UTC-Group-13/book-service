package org.example.bookservice.service;

import org.example.bookservice.dto.request.CategoryRequest;
import org.example.bookservice.dto.request.CategorySearchRequest;
import org.example.bookservice.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);

    Page<CategoryResponse> getAllCategories(CategorySearchRequest request);

    CategoryResponse getCategoryById(Integer id);

    CategoryResponse updateCategory(Integer id, CategoryRequest request);

    void deleteCategory(Integer id);
}
package org.example.bookservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.example.bookservice.dto.request.CategoryRequest;
import org.example.bookservice.dto.response.CategoryResponse;
import org.example.bookservice.entity.Category;
import org.example.bookservice.mapper.CategoryMapper;
import org.example.bookservice.repository.CategoryRepository;
import org.example.bookservice.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        // Optional uniqueness guard
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Category name already exists");
        }
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .deleteFlg(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public Page<CategoryResponse> getAllCategories(String name, Pageable pageable) {
        return categoryRepository
                .findAllWithFilters(name, pageable)
                .map(categoryMapper::toCategoryResponse);
    }

    @Override
    public CategoryResponse getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .filter(c -> Boolean.FALSE.equals(c.getDeleteFlg()))
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public CategoryResponse updateCategory(Integer id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .filter(c -> Boolean.FALSE.equals(c.getDeleteFlg()))
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        if (request.getName() != null) category.setName(request.getName());
        if (request.getDescription() != null) category.setDescription(request.getDescription());
        category.setUpdatedAt(LocalDateTime.now());

        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .filter(c -> Boolean.FALSE.equals(c.getDeleteFlg()))
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        category.setDeleteFlg(true); // soft delete
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }
}
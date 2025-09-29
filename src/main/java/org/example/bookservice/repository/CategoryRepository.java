package org.example.bookservice.repository;

import org.example.bookservice.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
    boolean existsByNameIgnoreCase(String name);

    @Query("""
            SELECT c FROM Category c
                WHERE c.deleteFlg = false
                AND ((:search IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')))
                OR (:search IS NULL OR LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%'))))
            """)
    Page<Category> findAllWithFilters(
            @Param("search") String search,
            Pageable pageable);

}

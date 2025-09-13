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
                AND (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
            """)
    Page<Category> findAllWithFilters(
            @Param("name") String name,
            Pageable pageable);

}

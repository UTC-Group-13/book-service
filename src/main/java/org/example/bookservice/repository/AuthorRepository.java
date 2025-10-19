package org.example.bookservice.repository;

import org.example.bookservice.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {
    boolean existsByFullNameIgnoreCase(String fullName);

    @Query("""
            SELECT a FROM Author a
            WHERE a.deleteFlg = false
               AND ((:search IS NULL OR LOWER(a.fullName) LIKE LOWER(CONCAT('%', :search, '%')))
               OR (:search IS NULL OR LOWER(a.nationality) LIKE LOWER(CONCAT('%', :search, '%')))
               OR (:search IS NULL OR LOWER(a.email) LIKE LOWER(CONCAT('%', :search, '%')))
               OR (:search IS NULL OR LOWER(a.biography) LIKE LOWER(CONCAT('%', :search, '%'))))
            """)
    Page<Author> findAllWithFilters(
            @Param("search") String search,
            Pageable pageable);


    Page<Author> findByFullNameContainingIgnoreCase(String name, Pageable pageable);

}
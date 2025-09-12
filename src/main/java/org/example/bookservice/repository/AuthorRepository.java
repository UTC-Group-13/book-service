package org.example.bookservice.repository;

import org.example.bookservice.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorRepository extends JpaRepository<Author, Integer>, JpaSpecificationExecutor<Author> {
    boolean existsByFullNameIgnoreCase(String fullName);

    @Query("SELECT a FROM Author a " +
            "LEFT JOIN a.books b " +
            "WHERE (:name IS NULL OR LOWER(a.fullName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:nationality IS NULL OR LOWER(a.nationality) LIKE LOWER(CONCAT('%', :nationality, '%'))) " +
            "AND (:email IS NULL OR LOWER(a.email) LIKE LOWER(CONCAT('%', :email, '%')))")
    Page<Author> findAllWithFilters(
            @Param("name") String name,
            @Param("nationality") String nationality,
            @Param("email") String email,
            Pageable pageable);

}
package org.example.bookservice.repository;

import org.example.bookservice.entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Integer> {
    @Modifying
    void deleteByBookId(Long bookId);
}
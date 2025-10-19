package org.example.bookservice.repository;

import org.example.bookservice.entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Integer> {

    void deleteByBookId(Long bookId);
}
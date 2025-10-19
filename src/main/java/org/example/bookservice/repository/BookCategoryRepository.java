package org.example.bookservice.repository;

import org.example.bookservice.entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Integer> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM BookCategory bc WHERE bc.bookId = :bookId")
    void deleteByBookId(Long bookId);

    List<BookCategory> findByBookId(Long bookId);
}
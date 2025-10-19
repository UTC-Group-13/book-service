package org.example.bookservice.repository;

import org.example.bookservice.entity.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Integer> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM BookAuthor ba WHERE ba.bookId = :bookId")
    void deleteByBookId(Long bookId);

    List<BookAuthor> findByBookId(Long bookId);
}
package org.example.bookservice.repository;

import org.example.bookservice.entity.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Integer> {

    void deleteByBookId(Long bookId);
}
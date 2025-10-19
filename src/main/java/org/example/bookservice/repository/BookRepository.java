package org.example.bookservice.repository;

import org.example.bookservice.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("""
            SELECT b FROM Book b
            WHERE
                (:search IS NULL OR LOWER(b.title) LIKE LOWER('%' || :search || '%')
                 OR LOWER(b.isbn) LIKE LOWER('%' || :search || '%'))
              AND (:publisherId IS NULL OR b.publisherId = :publisherId)
              AND (:publishYear IS NULL OR b.publishYear = :publishYear)
              AND (:minPrice IS NULL OR b.price >= :minPrice)
              AND (:maxPrice IS NULL OR b.price <= :maxPrice)
              AND (
                  :categoryIds IS NULL OR EXISTS (
                      SELECT 1 FROM BookCategory bc
                      WHERE bc.bookId = b.id AND bc.categoryId IN :categoryIds
                  )
              )
              AND (
                  :authorIds IS NULL OR EXISTS (
                      SELECT 1 FROM BookAuthor ba
                      WHERE ba.bookId = b.id AND ba.authorId IN :authorIds
                  )
            )
            ORDER BY b.id DESC
            """)
    Page<Book> findAllWithFilters(
            @Param("search") String search,
            @Param("publisherId") Integer publisherId,
            @Param("publishYear") Integer publishYear,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("categoryIds") Set<Long> categoryIds,
            @Param("authorIds") Set<Long> authorIds,
            Pageable pageable
    );

}

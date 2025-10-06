package org.example.bookservice.repository;

import org.example.bookservice.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query("""
            SELECT b FROM Book b
                LEFT JOIN Category c
                LEFT JOIN Author a
            WHERE (
                    (:search IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')))
                    OR (:search IS NULL OR b.isbn = :search)
                        )
                AND (:publisherId IS NULL OR b.publisher.id = :publisherId)
                AND (:publishYear IS NULL OR b.publishYear = :publishYear)
                AND (:minPrice IS NULL OR b.price >= :minPrice)
                AND (:maxPrice IS NULL OR b.price <= :maxPrice)
                AND (:categoryIds IS NULL OR c.id IN (:categoryIds))
                AND (:authorIds IS NULL OR a.id IN (:authorIds))
            """)
    Page<Book> findAllWithFilters(
            @Param("search") String search,
            @Param("publisherId") Integer publisherId,
            @Param("publishYear") Integer publishYear,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("categoryIds") Set<Integer> categoryIds,
            @Param("authorIds") Set<Integer> authorIds,
            Pageable pageable);

}

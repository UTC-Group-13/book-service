package org.example.bookservice.repository;

import org.example.bookservice.entity.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    boolean existsByNameIgnoreCase(String name);

    @Query("""
           SELECT p FROM Publisher p
           WHERE p.deleteFlg = false
             AND ((:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')))
             OR (:search IS NULL OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))))
           """)
    Page<Publisher> findAllWithFilters(String search, Pageable pageable);
}
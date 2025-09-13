package org.example.bookservice.repository;

import org.example.bookservice.entity.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Integer> {

    boolean existsByNameIgnoreCase(String name);

    @Query("""
           SELECT p FROM Publisher p
           WHERE p.deleteFlg = false
             AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
           """)
    Page<Publisher> findAllWithFilters(String name, Pageable pageable);
}
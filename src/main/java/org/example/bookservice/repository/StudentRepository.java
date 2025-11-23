package org.example.bookservice.repository;

import org.example.bookservice.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("""
        SELECT s FROM Student s
        WHERE s.deleteFlg = false
          AND ((:search IS NULL OR LOWER(s.code) LIKE LOWER(CONCAT('%', :search, '%')))
          OR (:search IS NULL OR LOWER(s.fullName) LIKE LOWER(CONCAT('%', :search, '%')))
          OR (:search IS NULL OR LOWER(s.email) LIKE LOWER(CONCAT('%', :search, '%')))
          OR (:search IS NULL OR LOWER(s.departmentCode) LIKE LOWER(CONCAT('%', :search, '%')))
          OR (:search IS NULL OR LOWER(s.classCode) LIKE LOWER(CONCAT('%', :search, '%'))))
        """)
    Page<Student> findAllWithFilters(
            @Param("search") String search,
            Pageable pageable
    );

    Student findStudentById(Long id);

    boolean existsByCode(String code);

    boolean existsByEmail(String email);
}
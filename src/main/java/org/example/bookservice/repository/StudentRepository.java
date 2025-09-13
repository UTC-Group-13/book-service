package org.example.bookservice.repository;

import org.example.bookservice.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("""
        SELECT s FROM Student s
        WHERE s.deleteFlg = false
          AND (:code IS NULL OR LOWER(s.code) LIKE LOWER(CONCAT('%', :code, '%')))
          AND (:fullName IS NULL OR LOWER(s.fullName) LIKE LOWER(CONCAT('%', :fullName, '%')))
          AND (:email IS NULL OR LOWER(s.email) LIKE LOWER(CONCAT('%', :email, '%')))
          AND (:phone IS NULL OR s.phone LIKE CONCAT('%', :phone, '%'))
          AND (:departmentCode IS NULL OR LOWER(s.departmentCode) LIKE LOWER(CONCAT('%', :departmentCode, '%')))
          AND (:classCode IS NULL OR LOWER(s.classCode) LIKE LOWER(CONCAT('%', :classCode, '%')))
        """)
    Page<Student> findAllWithFilters(
            String code,
            String fullName,
            String email,
            String phone,
            String departmentCode,
            String classCode,
            Pageable pageable
    );
}
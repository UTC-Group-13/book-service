package org.example.bookservice.repository;

import org.example.bookservice.entity.BookLoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, Integer> {

    @Query("""
        SELECT bl FROM BookLoan bl
        WHERE bl.deleteFlg = false
          AND ((:studentId IS NULL OR bl.studentId = :studentId)
          OR (:bookId IS NULL OR bl.bookId = :bookId)
          OR (:status IS NULL OR bl.status = :status)
          OR (:borrowFrom IS NULL OR bl.borrowDate >= :borrowFrom)
          OR (:borrowTo IS NULL OR bl.borrowDate <= :borrowTo)
          OR (:dueFrom IS NULL OR bl.dueDate >= :dueFrom)
          OR (:dueTo IS NULL OR bl.dueDate <= :dueTo)
          OR (:onlyNotReturned IS NULL OR (:onlyNotReturned = true AND bl.returnDate IS NULL) OR (:onlyNotReturned = false))
          OR (:onlyOverdue IS NULL OR (:onlyOverdue = true AND bl.returnDate IS NULL AND bl.dueDate < CURRENT_DATE) OR (:onlyOverdue = false)))
        """)
    Page<BookLoan> findAllWithFilters(
            Integer studentId,
            Integer bookId,
            Integer adminId,
            String status,
            LocalDate borrowFrom,
            LocalDate borrowTo,
            LocalDate dueFrom,
            LocalDate dueTo,
            Boolean onlyNotReturned,
            Boolean onlyOverdue,
            Pageable pageable
    );

    @Query("SELECT bl FROM BookLoan bl WHERE bl.dueDate < :date AND bl.status IN :status AND bl.deleteFlg = false")
    List<BookLoan> findExpiredLoans(@Param("date") LocalDate date, @Param("status") List<String> status);
}
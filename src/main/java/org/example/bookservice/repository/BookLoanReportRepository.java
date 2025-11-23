package org.example.bookservice.repository;

import org.example.bookservice.entity.BookLoanReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookLoanReportRepository extends JpaRepository<BookLoanReport, LocalDate> {
    Optional<BookLoanReport> findByReportDate(LocalDate date);

    @Query("SELECT r FROM BookLoanReport r WHERE r.reportDate BETWEEN :start AND :end ORDER BY r.reportDate ASC")
    List<BookLoanReport> findByDateRange(@Param("start") LocalDate start, @Param("end") LocalDate end);
}

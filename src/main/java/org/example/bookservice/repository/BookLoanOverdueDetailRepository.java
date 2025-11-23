package org.example.bookservice.repository;

import org.example.bookservice.entity.BookLoanOverdueDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookLoanOverdueDetailRepository extends JpaRepository<BookLoanOverdueDetail, Long> {
    void deleteByReportDate(LocalDate reportDate);

    List<BookLoanOverdueDetail> findByReportDate(LocalDate reportDate);

    @Query("""
        SELECT d 
        FROM BookLoanOverdueDetail d
        WHERE d.reportDate BETWEEN :start AND :end
        ORDER BY d.reportDate ASC
    """)
    List<BookLoanOverdueDetail> findByDateRange(@Param("start") LocalDate start, @Param("end") LocalDate end);
}


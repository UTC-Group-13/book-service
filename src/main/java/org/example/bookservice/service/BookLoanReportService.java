package org.example.bookservice.service;

import org.example.bookservice.entity.BookLoanOverdueDetail;
import org.example.bookservice.entity.BookLoanReport;

import java.time.LocalDate;
import java.util.List;

public interface BookLoanReportService {
    void generateDailyReport();

    List<BookLoanReport> getReportsByRange(LocalDate start, LocalDate end);

    List<BookLoanOverdueDetail> getOverdueByRange(LocalDate start, LocalDate end);
}

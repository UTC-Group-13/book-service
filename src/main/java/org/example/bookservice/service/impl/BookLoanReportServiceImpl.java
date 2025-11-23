package org.example.bookservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookservice.entity.BookLoan;
import org.example.bookservice.entity.BookLoanOverdueDetail;
import org.example.bookservice.entity.BookLoanReport;
import org.example.bookservice.repository.BookLoanOverdueDetailRepository;
import org.example.bookservice.repository.BookLoanReportRepository;
import org.example.bookservice.repository.BookLoanRepository;
import org.example.bookservice.service.BookLoanReportService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookLoanReportServiceImpl implements BookLoanReportService {
    private final BookLoanRepository bookLoanRepository;
    private final BookLoanReportRepository reportRepository;
    private final BookLoanOverdueDetailRepository overdueRepository;

    @Override
    public void generateDailyReport() {
        LocalDate reportDate = LocalDate.now().minusDays(1);
        log.info("▶ Đang tạo báo cáo mượn sách cho ngày {}", reportDate);

        // Xóa dữ liệu cũ
        overdueRepository.deleteByReportDate(reportDate);
        reportRepository.deleteById(reportDate);

        Integer totalBorrowed = bookLoanRepository.countBorrowed();
        Integer totalOverdue = bookLoanRepository.countOverdue(reportDate);
        Integer totalReturned = bookLoanRepository.countReturned(reportDate);

        // Tiền phạt: 1000đ/ngày quá hạn
        Double totalFeeEstimate = totalOverdue * 1000.0;
        // Lưu báo cáo tổng hợp
        BookLoanReport report = BookLoanReport.builder()
                .reportDate(reportDate)
                .totalBorrowed(totalBorrowed)
                .totalOverdue(totalOverdue)
                .totalReturned(totalReturned)
                .totalFeeEstimate(totalFeeEstimate)
                .createdAt(LocalDateTime.now())
                .build();

        reportRepository.save(report);

        // Lấy danh sách quá hạn
        List<BookLoan> overdueLoans = bookLoanRepository.findOverdueLoans(reportDate);

        // Lưu chi tiết quá hạn
        for (BookLoan loan : overdueLoans) {
            int daysOverdue = (int) (reportDate.toEpochDay() - loan.getDueDate().toEpochDay());

            BookLoanOverdueDetail detail = BookLoanOverdueDetail.builder()
                    .reportDate(reportDate)
                    .loanId(loan.getId())
                    .studentId(loan.getStudentId())
                    .bookId(loan.getBookId())
                    .dueDate(loan.getDueDate())
                    .borrowDate(loan.getBorrowDate())
                    .daysOverdue(daysOverdue)
                    .estimatedFee(daysOverdue * 1000.0)
                    .createdAt(LocalDateTime.now())
                    .build();

            overdueRepository.save(detail);
        }

        log.info("✔ Đã tạo báo cáo cho ngày {}", reportDate);
    }

    @Override
    public List<BookLoanReport> getReportsByRange(LocalDate start, LocalDate end) {
        return reportRepository.findByDateRange(start, end);
    }

    @Override
    public List<BookLoanOverdueDetail> getOverdueByRange(LocalDate start, LocalDate end) {
        return overdueRepository.findByDateRange(start, end);
    }
}

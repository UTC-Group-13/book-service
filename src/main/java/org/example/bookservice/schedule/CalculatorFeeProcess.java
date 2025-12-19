package org.example.bookservice.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookservice.constant.Status;
import org.example.bookservice.dto.response.BookResponse;
import org.example.bookservice.entity.BookLoan;
import org.example.bookservice.repository.BookLoanRepository;
import org.example.bookservice.service.BookService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalculatorFeeProcess {
    private final BookLoanRepository bookLoanRepository;
    private final BookService bookService;
    @Scheduled(cron = "0 0/1 * * * ?")
    private void process(){
        log.info("========= Bat dau tien trinh thanh toan phi tre han =========");
        List<BookLoan> bookLoans = bookLoanRepository.findExpiredLoans(LocalDate.now(), List.of(Status.BORROWING.getValue(), Status.LATE.getValue()));
        for (BookLoan bookLoan : bookLoans) {
            LocalDate today = LocalDate.now();
            BookResponse book = bookService.getBookById(bookLoan.getBookId());
            long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(bookLoan.getDueDate(), today);
            if(today == bookLoan.getReturnDate()){
                daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(bookLoan.getDueDate(), bookLoan.getReturnDate());
            }
            if (daysOverdue > 0) {
                long fee = book.getPrice().longValue() * daysOverdue / 100;
                bookLoan.setFee(BigDecimal.valueOf(fee));
                bookLoan.setStatus(Status.LATE.getValue());
                bookLoan.setUpdatedAt(LocalDateTime.now());
                bookLoanRepository.save(bookLoan);
                log.info("Cap nhap phi tre han ID: {}, Phi tre han: {}", bookLoan.getId(), fee);
            }
        }
        log.info("========= Ket thuc tien trinh tinh phi tre han =========");
    }
}

package org.example.bookservice.service;

import org.example.bookservice.dto.request.BookLoanRequest;
import org.example.bookservice.dto.response.BookLoanResponse;
import org.example.bookservice.entity.BookLoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface BookLoanService {

    Page<BookLoanResponse> getAllBookLoans(
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
            String search,
            Pageable pageable
    );

    BookLoanResponse createBookLoan(BookLoanRequest request);

    BookLoanResponse getBookLoanById(Long id);

    BookLoanResponse updateBookLoan(Long id, BookLoanRequest request);

    BookLoanResponse returnBookLoan(Long id, LocalDate returnDate);

    void deleteBookLoan(Long id);

    void deleteByBookId(Long bookId);

    boolean existsByBookIdAndStatusNot(Long bookId, String status);

    List<BookLoan> findExpiredLoans(LocalDate date, List<String> status);

    List<BookLoan> saveAll(List<BookLoan> bookLoans);
}
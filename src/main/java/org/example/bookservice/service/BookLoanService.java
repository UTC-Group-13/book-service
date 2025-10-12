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
            Pageable pageable
    );

    BookLoanResponse createBookLoan(BookLoanRequest request);

    BookLoanResponse getBookLoanById(Integer id);

    BookLoanResponse updateBookLoan(Integer id, BookLoanRequest request);

    BookLoanResponse returnBookLoan(Integer id, LocalDate returnDate);

    void deleteBookLoan(Integer id);

    List<BookLoan> findExpiredLoans(LocalDate date, List<String> status);

    List<BookLoan> saveAll(List<BookLoan> bookLoans);
}
package org.example.bookservice.service;

import org.example.bookservice.dto.request.BookLoanRequest;
import org.example.bookservice.dto.response.BookLoanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

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
}
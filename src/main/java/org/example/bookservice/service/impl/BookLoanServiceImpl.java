package org.example.bookservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.bookservice.dto.request.BookLoanRequest;
import org.example.bookservice.dto.response.BookLoanResponse;
import org.example.bookservice.entity.Admin;
import org.example.bookservice.entity.Book;
import org.example.bookservice.entity.BookLoan;
import org.example.bookservice.entity.Student;
import org.example.bookservice.mapper.BookLoanMapper;
import org.example.bookservice.repository.AdminRepository;
import org.example.bookservice.repository.BookLoanRepository;
import org.example.bookservice.repository.BookRepository;
import org.example.bookservice.repository.StudentRepository;
import org.example.bookservice.service.BookLoanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookLoanServiceImpl implements BookLoanService {

    private final BookLoanRepository bookLoanRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final BookLoanMapper bookLoanMapper;
    private final AdminRepository adminRepository;

    // You may externalize these as config later
    private static final String STATUS_BORROWED = "BORROWED";
    private static final String STATUS_RETURNED = "RETURNED";
    private static final BigDecimal DAILY_LATE_FEE = BigDecimal.ONE; // 1 unit per day overdue

    @Override
    public Page<BookLoanResponse> getAllBookLoans(
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
    ) {
        return bookLoanRepository
                .findAllWithFilters(studentId, bookId, adminId, status, borrowFrom, borrowTo, dueFrom, dueTo, onlyNotReturned, onlyOverdue, pageable)
                .map(bookLoanMapper::toBookLoanResponse);
    }

    @Override
    @Transactional
    public BookLoanResponse createBookLoan(BookLoanRequest request) {

        // Validate and adjust book inventory
        Book book = bookRepository.findById(request.getBookId())
                .filter(b -> Boolean.FALSE.equals(b.getDeleteFlg()))
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        if (book.getQuantity() == null || book.getQuantity() <= 0) {
            throw new IllegalStateException("Book is out of stock");
        }
        book.setQuantity(book.getQuantity() - 1);
        book.setUpdatedAt(LocalDateTime.now());
        bookRepository.save(book);

        // Validate related refs exist (Student, Admin)
        Student student = studentRepository.findStudentById(request.getStudentId());
        if (student == null) throw new EntityNotFoundException("Student not found");
        Admin admin = adminRepository.findAdminsById(request.getAdminId());
        if (admin == null) throw new EntityNotFoundException("Admin not found");

        LocalDateTime now = LocalDateTime.now();
        BookLoan loan = bookLoanMapper.toBookLoan(request);

        return bookLoanMapper.toBookLoanResponse(bookLoanRepository.save(loan));
    }

    @Override
    public BookLoanResponse getBookLoanById(Integer id) {
        BookLoan loan = bookLoanRepository.findById(id)
                .filter(l -> Boolean.FALSE.equals(l.getDeleteFlg()))
                .orElseThrow(() -> new EntityNotFoundException("BookLoan not found"));
        return bookLoanMapper.toBookLoanResponse(loan);
    }

    @Override
    @Transactional
    public BookLoanResponse updateBookLoan(Integer id, BookLoanRequest request) {
        BookLoan loan = bookLoanRepository.findById(id)
                .filter(l -> Boolean.FALSE.equals(l.getDeleteFlg()))
                .orElseThrow(() -> new EntityNotFoundException("BookLoan not found"));

        if (request.getAdminId() != null) {
            Admin admin = adminRepository.findAdminsById(request.getAdminId());
            if (admin == null) throw new EntityNotFoundException("Admin not found");
            loan.setAdmin(admin);
        }
        // For simplicity, studentId and bookId are immutable after creation

        loan.setUpdatedAt(LocalDateTime.now());
        return bookLoanMapper.toBookLoanResponse(bookLoanRepository.save(loan));
    }

    @Override
    @Transactional
    public BookLoanResponse returnBookLoan(Integer id, LocalDate returnDate) {
        BookLoan loan = bookLoanRepository.findById(id)
                .filter(l -> Boolean.FALSE.equals(l.getDeleteFlg()))
                .orElseThrow(() -> new EntityNotFoundException("BookLoan not found"));

        if (loan.getReturnDate() != null) {
            throw new IllegalStateException("Loan already returned");
        }

        LocalDate actualReturn = returnDate != null ? returnDate : LocalDate.now();
        if (actualReturn.isBefore(loan.getBorrowDate())) {
            throw new IllegalArgumentException("returnDate cannot be before borrowDate");
        }

        // Compute fee if overdue
        BigDecimal fee = BigDecimal.ZERO;
        if (loan.getDueDate() != null && actualReturn.isAfter(loan.getDueDate())) {
            long daysOverdue = loan.getDueDate().until(actualReturn).getDays();
            if (daysOverdue > 0) {
                fee = DAILY_LATE_FEE.multiply(BigDecimal.valueOf(daysOverdue));
            }
        }
        loan.setReturnDate(actualReturn);
        loan.setStatus(STATUS_RETURNED);
        loan.setFee(fee);
        loan.setUpdatedAt(LocalDateTime.now());

        // Return inventory to the book
        Book book = loan.getBook();
        book.setQuantity((book.getQuantity() == null ? 0 : book.getQuantity()) + 1);
        book.setUpdatedAt(LocalDateTime.now());
        bookRepository.save(book);

        return bookLoanMapper.toBookLoanResponse(bookLoanRepository.save(loan));
    }

    @Override
    @Transactional
    public void deleteBookLoan(Integer id) {
        BookLoan loan = bookLoanRepository.findById(id)
                .filter(l -> Boolean.FALSE.equals(l.getDeleteFlg()))
                .orElseThrow(() -> new EntityNotFoundException("BookLoan not found"));

        loan.setDeleteFlg(true);
        loan.setUpdatedAt(LocalDateTime.now());
        bookLoanRepository.save(loan);
    }
}
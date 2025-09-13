package org.example.bookservice.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.bookservice.dto.request.BookLoanRequest;
import org.example.bookservice.dto.response.BookLoanResponse;
import org.example.bookservice.entity.Admin;
import org.example.bookservice.entity.Book;
import org.example.bookservice.entity.BookLoan;
import org.example.bookservice.entity.Student;
import org.example.bookservice.repository.BookLoanRepository;
import org.example.bookservice.repository.BookRepository;
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
    private final EntityManager entityManager;

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
                .map(this::toResponse);
    }

    @Override
    @Transactional
    public BookLoanResponse createBookLoan(BookLoanRequest request) {
        LocalDate borrow = request.getBorrowDate() != null ? request.getBorrowDate() : LocalDate.now();

        if (request.getStudentId() == null || request.getBookId() == null || request.getAdminId() == null) {
            throw new IllegalArgumentException("studentId, bookId and adminId are required");
        }
        if (request.getDueDate() == null) {
            throw new IllegalArgumentException("dueDate is required");
        }
        if (request.getDueDate().isBefore(borrow)) {
            throw new IllegalArgumentException("dueDate must be on or after borrowDate");
        }

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
        Student student = entityManager.find(Student.class, request.getStudentId());
        if (student == null) throw new EntityNotFoundException("Student not found");
        Admin admin = entityManager.find(Admin.class, request.getAdminId());
        if (admin == null) throw new EntityNotFoundException("Admin not found");

        LocalDateTime now = LocalDateTime.now();
        BookLoan loan = BookLoan.builder()
                .student(student)
                .book(book)
                .admin(admin)
                .borrowDate(borrow)
                .dueDate(request.getDueDate())
                .status(STATUS_BORROWED)
                .fee(BigDecimal.ZERO)
                .deleteFlg(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return toResponse(bookLoanRepository.save(loan));
    }

    @Override
    public BookLoanResponse getBookLoanById(Integer id) {
        BookLoan loan = bookLoanRepository.findById(id)
                .filter(l -> Boolean.FALSE.equals(l.getDeleteFlg()))
                .orElseThrow(() -> new EntityNotFoundException("BookLoan not found"));
        return toResponse(loan);
    }

    @Override
    @Transactional
    public BookLoanResponse updateBookLoan(Integer id, BookLoanRequest request) {
        BookLoan loan = bookLoanRepository.findById(id)
                .filter(l -> Boolean.FALSE.equals(l.getDeleteFlg()))
                .orElseThrow(() -> new EntityNotFoundException("BookLoan not found"));

        if (loan.getReturnDate() != null) {
            throw new IllegalStateException("Returned loans cannot be updated");
        }

        if (request.getDueDate() != null) {
            if (request.getDueDate().isBefore(loan.getBorrowDate())) {
                throw new IllegalArgumentException("dueDate must be on or after borrowDate");
            }
            loan.setDueDate(request.getDueDate());
        }
        if (request.getAdminId() != null) {
            Admin admin = entityManager.find(Admin.class, request.getAdminId());
            if (admin == null) throw new EntityNotFoundException("Admin not found");
            loan.setAdmin(admin);
        }
        // For simplicity, studentId and bookId are immutable after creation

        loan.setUpdatedAt(LocalDateTime.now());
        return toResponse(bookLoanRepository.save(loan));
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

        return toResponse(bookLoanRepository.save(loan));
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

    private BookLoanResponse toResponse(BookLoan entity) {
        return BookLoanResponse.builder()
                .id(entity.getId())
                .studentId(entity.getStudent() != null ? entity.getStudent().getId() : null)
                .bookId(entity.getBook() != null ? entity.getBook().getId() : null)
                .adminId(entity.getAdmin() != null ? entity.getAdmin().getId() : null)
                .borrowDate(entity.getBorrowDate())
                .dueDate(entity.getDueDate())
                .returnDate(entity.getReturnDate())
                .status(entity.getStatus())
                .fee(entity.getFee())
                .deleteFlg(entity.getDeleteFlg())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
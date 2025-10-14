package org.example.bookservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.bookservice.dto.exception.BusinessException;
import org.example.bookservice.dto.request.BookLoanRequest;
import org.example.bookservice.dto.request.SendEmailRequest;
import org.example.bookservice.dto.response.BookLoanResponse;
import org.example.bookservice.entity.Book;
import org.example.bookservice.entity.BookLoan;
import org.example.bookservice.entity.Student;
import org.example.bookservice.mapper.BookLoanMapper;
import org.example.bookservice.repository.BookLoanRepository;
import org.example.bookservice.repository.BookRepository;
import org.example.bookservice.repository.StudentRepository;
import org.example.bookservice.service.BookLoanService;
import org.example.bookservice.service.EmailService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookLoanServiceImpl implements BookLoanService {

    private final BookLoanRepository bookLoanRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final BookLoanMapper bookLoanMapper;
    private final EmailService emailService;

    public BookLoanServiceImpl(
            BookLoanRepository bookLoanRepository,
            BookRepository bookRepository,
            StudentRepository studentRepository,
            BookLoanMapper bookLoanMapper,
            @Lazy EmailService emailService
    ) {
        this.bookLoanRepository = bookLoanRepository;
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
        this.bookLoanMapper = bookLoanMapper;
        this.emailService = emailService;
    }

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
        BookLoan loan = bookLoanMapper.toBookLoan(request);
        loan.setDeleteFlg(false);
        loan.setStatus(STATUS_BORROWED);
        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.setBookId(request.getBookId());
        sendEmailRequest.setStudentId(request.getStudentId());
        sendEmailRequest.setDueDate(request.getDueDate());
        emailService.sendEmail(sendEmailRequest);

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
        // For simplicity, studentId and bookId are immutable after creation
        if (Objects.equals(request.getStatus(), STATUS_RETURNED)) {
            loan.setReturnDate(LocalDate.now());
        }
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
            throw new BusinessException("Loan already returned", "LOAN_ALREADY_RETURNED");
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
        Optional<Book> optBook = bookRepository.findById(loan.getBookId());
        if(optBook.isPresent()){
            Book book = optBook.get();
            book.setQuantity((book.getQuantity() == null ? 0 : book.getQuantity()) + 1);
            book.setUpdatedAt(LocalDateTime.now());
            bookRepository.save(book);
        }

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

    @Override
    @Transactional(readOnly = true)
    public List<BookLoan> findExpiredLoans(LocalDate date, List<String> status) {
        return bookLoanRepository.findExpiredLoans(date, status);
    }

    @Override
    public List<BookLoan> saveAll(List<BookLoan> bookLoans) {
        if(CollectionUtils.isEmpty(bookLoans)){
            throw new BusinessException("BookLoans is empty", "BOOKLOANS_EMPTY");
        }
        return bookLoanRepository.saveAll(bookLoans);
    }
}
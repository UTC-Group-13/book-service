package org.example.bookservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.bookservice.dto.request.BookLoanRequest;
import org.example.bookservice.dto.response.BookLoanResponse;
import org.example.bookservice.service.BookLoanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/book-loans")
@RequiredArgsConstructor
@Tag(name = "BookLoan API", description = "Manage book loans through CRUD + search API endpoints")
public class BookLoanController {

    private final BookLoanService bookLoanService;

    @Operation(summary = "Get all book loans", description = "Returns a paginated list of loans with optional filters")
    @GetMapping
    public ResponseEntity<Page<BookLoanResponse>> getAllBookLoans(
            @RequestParam(required = false) Integer studentId,
            @RequestParam(required = false) Integer bookId,
            @RequestParam(required = false) Integer adminId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate borrowFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate borrowTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueTo,
            @RequestParam(required = false) Boolean onlyNotReturned,
            @RequestParam(required = false) Boolean onlyOverdue,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                bookLoanService.getAllBookLoans(
                        studentId, bookId, adminId, status,
                        borrowFrom, borrowTo, dueFrom, dueTo,
                        onlyNotReturned, onlyOverdue, pageable
                )
        );
    }

    @Operation(summary = "Create a book loan", description = "Creates a new loan; decrements book stock")
    @PostMapping
    public ResponseEntity<BookLoanResponse> createBookLoan(@RequestBody BookLoanRequest request) {
        return ResponseEntity.status(201).body(bookLoanService.createBookLoan(request));
    }

    @Operation(summary = "Get a book loan by ID", description = "Fetch a single loan by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BookLoanResponse> getBookLoanById(@PathVariable Integer id) {
        return ResponseEntity.ok(bookLoanService.getBookLoanById(id));
    }

    @Operation(summary = "Update a book loan", description = "Update due date or admin of an existing loan")
    @PutMapping("/{id}")
    public ResponseEntity<BookLoanResponse> updateBookLoan(@PathVariable Integer id, @RequestBody BookLoanRequest request) {
        return ResponseEntity.ok(bookLoanService.updateBookLoan(id, request));
    }

    @Operation(summary = "Return a book loan", description = "Marks a loan as returned; increments book stock; calculates late fee if overdue")
    @PostMapping("/{id}/return")
    public ResponseEntity<BookLoanResponse> returnBookLoan(
            @PathVariable Integer id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate
    ) {
        return ResponseEntity.ok(bookLoanService.returnBookLoan(id, returnDate));
    }

    @Operation(summary = "Delete a book loan", description = "Soft deletes a loan")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookLoan(@PathVariable Integer id) {
        bookLoanService.deleteBookLoan(id);
        return ResponseEntity.noContent().build();
    }
}
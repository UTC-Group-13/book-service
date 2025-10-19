package org.example.bookservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.bookservice.dto.request.BookRequest;
import org.example.bookservice.dto.request.BookSearchRequest;
import org.example.bookservice.dto.response.BookResponse;
import org.example.bookservice.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
@Tag(name = "Book API", description = "Manage books through CRUD API endpoints")
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Get all books", description = "Returns a paginated and filtered list of all books")
    @PostMapping("/search")
    public ResponseEntity<Page<BookResponse>> getAllBooks(@RequestBody BookSearchRequest request) {

        Page<BookResponse> books = bookService.getAllBooks(request);
        return ResponseEntity.ok(books);
    }


    @Operation(summary = "Create a book", description = "Creates a new book and returns the created book details")
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest bookRequest) {
        BookResponse createdBook = bookService.createBook(bookRequest);
        return ResponseEntity.status(201).body(createdBook);
    }

    @Operation(summary = "Get a book by ID", description = "Fetches the details of a single book by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Integer id) {
        BookResponse book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Update a book", description = "Updates details of an existing book by its ID")
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@Valid @PathVariable Integer id, @RequestBody BookRequest bookRequest) {
        BookResponse updatedBook = bookService.updateBook(id, bookRequest);
        return ResponseEntity.ok(updatedBook);
    }

    @Operation(summary = "Delete a book", description = "Deletes a book by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
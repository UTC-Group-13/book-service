package org.example.bookservice.service;

import org.example.bookservice.dto.request.BookRequest;
import org.example.bookservice.dto.response.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface BookService {
    BookResponse createBook(BookRequest bookRequest);

    Page<BookResponse> getAllBooks(
            String title,
            String isbn,
            Integer publisherId,
            Integer publishYear,
            Integer minPrice,
            Integer maxPrice,
            Set<Integer> categoryIds,
            Set<Integer> authorIds,
            Pageable pageable);

    BookResponse getBookById(Integer id);

    BookResponse updateBook(Integer id, BookRequest bookRequest);

    void deleteBook(Integer id);
}
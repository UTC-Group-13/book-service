package org.example.bookservice.service;

import org.example.bookservice.dto.request.BookRequest;
import org.example.bookservice.dto.request.BookSearchRequest;
import org.example.bookservice.dto.response.BookResponse;
import org.example.bookservice.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface BookService {
    BookResponse createBook(BookRequest bookRequest);

    Page<BookResponse> getAllBooks(BookSearchRequest request);

    BookResponse getBookById(Integer id);

    BookResponse updateBook(Integer id, BookRequest bookRequest);

    void deleteBook(Integer id);

    Book findById(Integer id);

    List<Book> findAllByIds(List<Integer> ids);
}
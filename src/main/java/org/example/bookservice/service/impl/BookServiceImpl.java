package org.example.bookservice.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookservice.dto.request.BookRequest;
import org.example.bookservice.dto.request.BookSearchRequest;
import org.example.bookservice.dto.response.BookResponse;
import org.example.bookservice.entity.Book;
import org.example.bookservice.mapper.BookMapper;
import org.example.bookservice.repository.BookRepository;
import org.example.bookservice.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponse createBook(BookRequest request) {
        Book book = bookMapper.toBook(request);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toBookResponse(savedBook);
    }

    @Override
    public Page<BookResponse> getAllBooks(BookSearchRequest request) {

        return bookRepository.findAllWithFilters(request.getSearch(), request.getPublisherId(), request.getPublishYear(),
                        request.getMinPrice(), request.getMaxPrice(), request.getCategoryIds(), request.getAuthorIds(), request.toPageable())
                .map(bookMapper::toBookResponse);
    }


    @Override
    public BookResponse getBookById(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return bookMapper.toBookResponse(book);
    }

    @Override
    public BookResponse updateBook(Integer id, BookRequest request) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Update fields
        existingBook.setTitle(request.getTitle());
        existingBook.setIsbn(request.getIsbn());
        existingBook.setQuantity(request.getQuantity());
        // (Handle other fields similarly)

        Book updatedBook = bookRepository.save(existingBook);
        return bookMapper.toBookResponse(updatedBook);
    }

    @Override
    public void deleteBook(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        bookRepository.delete(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Book findById(Integer id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

}

package org.example.bookservice.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookservice.dto.request.BookRequest;
import org.example.bookservice.dto.request.BookSearchRequest;
import org.example.bookservice.dto.response.BookResponse;
import org.example.bookservice.entity.Book;
import org.example.bookservice.entity.BookAuthor;
import org.example.bookservice.entity.BookCategory;
import org.example.bookservice.mapper.BookMapper;
import org.example.bookservice.repository.BookAuthorRepository;
import org.example.bookservice.repository.BookCategoryRepository;
import org.example.bookservice.repository.BookRepository;
import org.example.bookservice.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookCategoryRepository bookCategoryRepository;
    private final BookAuthorRepository bookAuthorRepository;

    @Override
    @Transactional
    public BookResponse createBook(BookRequest request) {
        Book book = bookMapper.toBook(request);
        Book savedBook = bookRepository.save(book);

        // Create book categories
        Set<BookCategory> bookCategories = request.getCategoryIds().stream()
                .map(categoryId -> BookCategory.builder()
                        .bookId(savedBook.getId())
                        .categoryId(categoryId)
                        .build())
                .collect(Collectors.toSet());
        bookCategoryRepository.saveAll(bookCategories);

        // Create book authors
        Set<BookAuthor> bookAuthors = request.getAuthorIds().stream()
                .map(authorId -> BookAuthor.builder()
                        .bookId(savedBook.getId())
                        .authorId(authorId
                        )
                        .build())
                .collect(Collectors.toSet());
        bookAuthorRepository.saveAll(bookAuthors);

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
    @Transactional
    public BookResponse updateBook(Integer id, BookRequest request) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Update fields
        existingBook.setTitle(request.getTitle());
        existingBook.setIsbn(request.getIsbn());
        existingBook.setPublishYear(request.getPublishYear());
        existingBook.setLanguage(request.getLanguage());
        existingBook.setQuantity(request.getQuantity());
        existingBook.setPrice(request.getPrice());
        existingBook.setDescription(request.getDescription());
        existingBook.setCoverImage(request.getCoverImage());
        existingBook.setPublisherId(request.getPublisherId());
        // (Handle other fields similarly)

        // Update categories
        bookCategoryRepository.deleteByBookId(existingBook.getId());
        Set<BookCategory> newCategories = request.getCategoryIds().stream()
                .map(categoryId -> BookCategory.builder()
                        .bookId(existingBook.getId())
                        .categoryId(categoryId)
                        .build())
                .collect(Collectors.toSet());
        bookCategoryRepository.saveAll(newCategories);

        // Update authors
        bookAuthorRepository.deleteByBookId(existingBook.getId());
        Set<BookAuthor> newAuthors = request.getAuthorIds().stream()
                .map(authorId -> BookAuthor.builder()
                        .bookId(existingBook.getId())
                        .authorId(authorId)
                        .build())
                .collect(Collectors.toSet());
        bookAuthorRepository.saveAll(newAuthors);

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

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAllByIds(List<Integer> ids) {
        return bookRepository.findAllById(ids);
    }

}

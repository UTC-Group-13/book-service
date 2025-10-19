package org.example.bookservice.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookservice.dto.request.BookRequest;
import org.example.bookservice.dto.request.BookSearchRequest;
import org.example.bookservice.dto.response.AuthorResponse;
import org.example.bookservice.dto.response.BookResponse;
import org.example.bookservice.dto.response.CategoryResponse;
import org.example.bookservice.entity.*;
import org.example.bookservice.mapper.AuthorMapper;
import org.example.bookservice.mapper.BookMapper;
import org.example.bookservice.mapper.CategoryMapper;
import org.example.bookservice.mapper.PublisherMapper;
import org.example.bookservice.repository.*;
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
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    @Override
    @Transactional
    public BookResponse createBook(BookRequest request) {
        Book book = bookMapper.toBook(request);
        book.setDeleteFlg(false);
        Book savedBook = bookRepository.save(book);

        Publisher publisher = publisherRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
        if (categories.isEmpty()) {
            throw new IllegalArgumentException("At least one category must be specified");
        }

        List<Author> authors = authorRepository.findAllById(request.getAuthorIds());
        if (authors.isEmpty()) {
            throw new IllegalArgumentException("At least one author must be specified");
        }

        // Create book categories
        Set<BookCategory> bookCategories = categories.stream()
                .map(category -> BookCategory.builder()
                        .bookId(savedBook.getId())
                        .categoryId(category.getId())
                        .build())
                .collect(Collectors.toSet());
        bookCategoryRepository.saveAll(bookCategories);

        // Create book authors
        Set<BookAuthor> bookAuthors = authors.stream()
                .map(author -> BookAuthor.builder()
                        .bookId(savedBook.getId())
                        .authorId(author.getId())
                        .build())
                .collect(Collectors.toSet());
        bookAuthorRepository.saveAll(bookAuthors);
        BookResponse response = bookMapper.toBookResponse(savedBook);
        response.setPublisher(publisherMapper.toPublisherResponse(publisher));
        response.setAuthors(authorMapper.toResponseList(authors));
        response.setCategories(categoryMapper.toResponseList(categories));
        return response;
    }

    @Override
    public Page<BookResponse> getAllBooks(BookSearchRequest request) {

        Page<Book> books = bookRepository.findAllWithFilters(
                request.getSearch(),
                request.getPublisherId(),
                request.getPublishYear(),
                request.getMinPrice(),
                request.getMaxPrice(),
                request.getCategoryIds(),
                request.getAuthorIds(),
                request.toPageable()
        );

        return books.map(book -> {
            BookResponse bookResponse = bookMapper.toBookResponse(book);

            // Load categories
            List<BookCategory> bookCategories = bookCategoryRepository.findByBookId(book.getId());
            List<Long> categoryIds = bookCategories.stream()
                    .map(BookCategory::getCategoryId)
                    .collect(Collectors.toList());

            if (!categoryIds.isEmpty()) {
                List<Category> categories = categoryRepository.findAllById(categoryIds);
                List<CategoryResponse> categoryResponses = categories.stream()
                        .map(categoryMapper::toCategoryResponse)
                        .collect(Collectors.toList());
                bookResponse.setCategories(categoryResponses);
            }

            // Load authors
            List<BookAuthor> bookAuthors = bookAuthorRepository.findByBookId(book.getId());
            List<Long> authorIds = bookAuthors.stream()
                    .map(BookAuthor::getAuthorId)
                    .collect(Collectors.toList());

            if (!authorIds.isEmpty()) {
                List<Author> authors = authorRepository.findAllById(authorIds);
                List<AuthorResponse> authorResponses = authors.stream()
                        .map(authorMapper::toAuthorResponse)
                        .collect(Collectors.toList());
                bookResponse.setAuthors(authorResponses);
            }
            Publisher publisher = publisherRepository.findById(book.getPublisherId())
                    .orElseThrow(() -> new RuntimeException("Publisher not found"));
            bookResponse.setPublisher(publisherMapper.toPublisherResponse(publisher));
            return bookResponse;
        });
    }


    @Override
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return bookMapper.toBookResponse(book);
    }

    @Override
    @Transactional
    public BookResponse updateBook(Long id, BookRequest request) {
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
        existingBook.setDeleteFlg(existingBook.getDeleteFlg());
        // (Handle other fields similarly)

        // Update categories
        bookCategoryRepository.deleteByBookId(existingBook.getId());
        bookCategoryRepository.flush();
        List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
        Set<BookCategory> newCategories = categories.stream()
                .map(category -> BookCategory.builder()
                        .bookId(existingBook.getId())
                        .categoryId(category.getId())
                        .build())
                .collect(Collectors.toSet());
        bookCategoryRepository.saveAll(newCategories);

        // Update authors
        bookAuthorRepository.deleteByBookId(existingBook.getId());
        bookAuthorRepository.flush();
        List<Author> authors = authorRepository.findAllById(request.getAuthorIds());
        Set<BookAuthor> newAuthors = authors.stream()
                .map(author -> BookAuthor.builder()
                        .bookId(existingBook.getId())
                        .authorId(author.getId())
                        .build())
                .collect(Collectors.toSet());
        bookAuthorRepository.saveAll(newAuthors);

        Book updatedBook = bookRepository.save(existingBook);
        BookResponse response = bookMapper.toBookResponse(updatedBook);
        response.setAuthors(authorMapper.toResponseList(authors));
        response.setCategories(categoryMapper.toResponseList(categories));
        return response;
    }

    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        bookRepository.delete(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAllByIds(List<Long> ids) {
        return bookRepository.findAllById(ids);
    }

}

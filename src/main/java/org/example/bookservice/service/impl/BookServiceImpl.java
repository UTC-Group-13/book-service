package org.example.bookservice.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookservice.dto.request.BookRequest;
import org.example.bookservice.dto.response.AuthorResponse;
import org.example.bookservice.dto.response.BookResponse;
import org.example.bookservice.dto.response.CategoryResponse;
import org.example.bookservice.dto.response.PublisherResponse;
import org.example.bookservice.entity.Author;
import org.example.bookservice.entity.Book;
import org.example.bookservice.entity.Category;
import org.example.bookservice.entity.Publisher;
import org.example.bookservice.repository.BookRepository;
import org.example.bookservice.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public BookResponse createBook(BookRequest request) {
        Book book = mapToEntity(request);
        Book savedBook = bookRepository.save(book);
        return mapToResponse(savedBook);
    }

    @Override
    public Page<BookResponse> getAllBooks(
            String title,
            String isbn,
            Integer publisherId,
            Integer publishYear,
            Integer minPrice,
            Integer maxPrice,
            Set<Integer> categoryIds,
            Set<Integer> authorIds,
            Pageable pageable) {

        // Perform filtering using custom repository or specifications
        return bookRepository.findAllWithFilters(title, isbn, publisherId, publishYear, minPrice, maxPrice, categoryIds, authorIds, pageable)
                .map(this::mapToResponse);
    }


    @Override
    public BookResponse getBookById(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return mapToResponse(book);
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
        return mapToResponse(updatedBook);
    }

    @Override
    public void deleteBook(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        bookRepository.delete(book);
    }

    private Book mapToEntity(BookRequest request) {
        // Map BookRequest DTO to Book entity (populate publisher, categories, etc.)
        return Book.builder()
                .title(request.getTitle())
                .isbn(request.getIsbn())
                .quantity(request.getQuantity())
                .build();
    }

    private BookResponse mapToResponse(Book book) {
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setIsbn(book.getIsbn());
        response.setPublishYear(book.getPublishYear());
        response.setLanguage(book.getLanguage());
        response.setQuantity(book.getQuantity());
        response.setPrice(book.getPrice());
        response.setDescription(book.getDescription());
        response.setCoverImage(book.getCoverImage());
        response.setDeleteFlg(book.getDeleteFlg());
        response.setCreatedAt(book.getCreatedAt());
        response.setUpdatedAt(book.getUpdatedAt());
        response.setPublisher(book.getPublisher() != null ? mapPublisherToResponse(book.getPublisher()) : null);
        response.setCategories(book.getCategories() != null ? book.getCategories().stream().map(this::mapCategoryToResponse).collect(Collectors.toSet()) : null);
        response.setAuthors(book.getAuthors() != null ? book.getAuthors().stream().map(this::mapAuthorToResponse).collect(Collectors.toSet()) : null);
        return response;
    }

    private PublisherResponse mapPublisherToResponse(Publisher publisher) {
        PublisherResponse response = new PublisherResponse();
        response.setId(publisher.getId());
        response.setName(publisher.getName());
        return response;
    }

    private CategoryResponse mapCategoryToResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        return response;
    }

    private AuthorResponse mapAuthorToResponse(Author author) {
        AuthorResponse response = new AuthorResponse();
        response.setId(author.getId());
        response.setName(author.getFullName());
        return response;
    }


}

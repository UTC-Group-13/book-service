package org.example.bookservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.bookservice.dto.request.AuthorRequest;
import org.example.bookservice.dto.response.AuthorResponse;
import org.example.bookservice.entity.Author;
import org.example.bookservice.repository.AuthorRepository;
import org.example.bookservice.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public AuthorResponse createAuthor(AuthorRequest request) {
        if (request.getFullName() == null || request.getFullName().isBlank()) {
            throw new IllegalArgumentException("Author fullName is required");
        }
        // Optional uniqueness guard
        if (authorRepository.existsByFullNameIgnoreCase(request.getFullName())) {
            throw new IllegalArgumentException("Author fullName already exists");
        }

        Author author = Author.builder()
                .fullName(request.getFullName())
                .birthDate(request.getBirthDate())
                .nationality(request.getNationality())
                .biography(request.getBiography())
                .email(request.getEmail())
                .deleteFlg(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return toResponse(authorRepository.save(author));
    }

    @Override
    public Page<AuthorResponse> getAllAuthors(String name, String nationality, String email, Pageable pageable) {
        return authorRepository
                .findAllWithFilters(name, nationality, email, pageable)
                .map(this::toResponse);
    }


    @Override
    public AuthorResponse getAuthorById(Integer id) {
        Author author = authorRepository.findById(id)
                .filter(a -> Boolean.FALSE.equals(a.getDeleteFlg()))
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        return toResponse(author);
    }

    @Override
    public AuthorResponse updateAuthor(Integer id, AuthorRequest request) {
        Author author = authorRepository.findById(id)
                .filter(a -> Boolean.FALSE.equals(a.getDeleteFlg()))
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));

        if (request.getFullName() != null) author.setFullName(request.getFullName());
        if (request.getBirthDate() != null) author.setBirthDate(request.getBirthDate());
        if (request.getNationality() != null) author.setNationality(request.getNationality());
        if (request.getBiography() != null) author.setBiography(request.getBiography());
        if (request.getEmail() != null) author.setEmail(request.getEmail());
        author.setUpdatedAt(LocalDateTime.now());

        return toResponse(authorRepository.save(author));
    }

    @Override
    public void deleteAuthor(Integer id) {
        Author author = authorRepository.findById(id)
                .filter(a -> Boolean.FALSE.equals(a.getDeleteFlg()))
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        author.setDeleteFlg(true); // soft delete
        author.setUpdatedAt(LocalDateTime.now());
        authorRepository.save(author);
    }

    private AuthorResponse toResponse(Author entity) {
        AuthorResponse resp = new AuthorResponse();
        resp.setId(entity.getId());
        resp.setFullName(entity.getFullName());
        return resp;
    }
}
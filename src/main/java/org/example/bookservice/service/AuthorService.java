package org.example.bookservice.service;

import org.example.bookservice.dto.request.AuthorRequest;
import org.example.bookservice.dto.response.AuthorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {
    AuthorResponse createAuthor(AuthorRequest request);

    Page<AuthorResponse> getAllAuthors(String name, String nationality, String email, Pageable pageable);

    AuthorResponse getAuthorById(Integer id);

    AuthorResponse updateAuthor(Integer id, AuthorRequest request);

    void deleteAuthor(Integer id);
}
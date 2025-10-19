package org.example.bookservice.service;

import org.example.bookservice.dto.request.AuthorRequest;
import org.example.bookservice.dto.request.AuthorSearchRequest;
import org.example.bookservice.dto.response.AuthorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {
    AuthorResponse createAuthor(AuthorRequest request);

    Page<AuthorResponse> getAllAuthors(AuthorSearchRequest request);

    AuthorResponse getAuthorById(Long id);

    AuthorResponse updateAuthor(Long id, AuthorRequest request);

    void deleteAuthor(Long id);

}
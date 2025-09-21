package org.example.bookservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookservice.constant.ErrorCode;
import org.example.bookservice.dto.exception.BusinessException;
import org.example.bookservice.dto.request.AuthorRequest;
import org.example.bookservice.dto.request.AuthorSearchRequest;
import org.example.bookservice.dto.response.AuthorResponse;
import org.example.bookservice.entity.Author;
import org.example.bookservice.mapper.AuthorMapper;
import org.example.bookservice.repository.AuthorRepository;
import org.example.bookservice.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Override
    public AuthorResponse createAuthor(AuthorRequest request) {
        if (authorRepository.existsByFullNameIgnoreCase(request.getFullName())) {
            throw new BusinessException(ErrorCode.AUTHOR_ALREADY_EXISTS.getCode(), ErrorCode.AUTHOR_ALREADY_EXISTS.getMessage());
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

        return authorMapper.toAuthorResponse(authorRepository.save(author));
    }

    @Override
    public Page<AuthorResponse> getAllAuthors(AuthorSearchRequest request) {
        return authorRepository
                .findAllWithFilters(request.getFullName(), request.getNationality(), request.getEmail(), request.getPageable())
                .map(authorMapper::toAuthorResponse);
    }


    @Override
    public AuthorResponse getAuthorById(Integer id) {
        Author author = authorRepository.findById(id)
                .filter(a -> Boolean.FALSE.equals(a.getDeleteFlg()))
                .orElseThrow(() -> new BusinessException(ErrorCode.AUTHOR_NOT_FOUND.getMessage(),
                        ErrorCode.AUTHOR_NOT_FOUND.getCode()));
        return authorMapper.toAuthorResponse(author);
    }

    @Override
    public AuthorResponse updateAuthor(Integer id, AuthorRequest request) {
        Author author = authorRepository.findById(id)
                .filter(a -> Boolean.FALSE.equals(a.getDeleteFlg()))
                .orElseThrow(() -> new BusinessException(ErrorCode.AUTHOR_NOT_FOUND.getMessage(),
                        ErrorCode.AUTHOR_NOT_FOUND.getCode()));
        author.setUpdatedAt(LocalDateTime.now());

        return authorMapper.toAuthorResponse(authorRepository.save(author));
    }

    @Override
    public void deleteAuthor(Integer id) {
        Author author = authorRepository.findById(id)
                .filter(a -> Boolean.FALSE.equals(a.getDeleteFlg()))
                .orElseThrow(() -> new BusinessException(ErrorCode.AUTHOR_NOT_FOUND.getMessage(),
                        ErrorCode.AUTHOR_NOT_FOUND.getCode()));
        author.setDeleteFlg(true); // soft delete
        author.setUpdatedAt(LocalDateTime.now());
        authorRepository.save(author);
    }
}
package org.example.bookservice.mapper;

import org.example.bookservice.dto.request.AuthorRequest;
import org.example.bookservice.dto.response.AuthorResponse;
import org.example.bookservice.entity.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // để Spring quản lý
public interface AuthorMapper {
    AuthorResponse toAuthorResponse(Author author);

    Author toAuthor(AuthorRequest authorRequest);
}

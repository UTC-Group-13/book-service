package org.example.bookservice.mapper;

import org.example.bookservice.dto.request.BookRequest;
import org.example.bookservice.dto.response.BookResponse;
import org.example.bookservice.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // để Spring quản lý
public interface BookMapper {
    BookResponse toBookResponse(Book book);

    Book toBook(BookRequest bookRequest);
}

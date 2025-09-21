package org.example.bookservice.mapper;

import org.example.bookservice.dto.request.BookLoanRequest;
import org.example.bookservice.dto.response.BookLoanResponse;
import org.example.bookservice.entity.BookLoan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // để Spring quản lý
public interface BookLoanMapper {
    BookLoanResponse toBookLoanResponse(BookLoan BookLoan);

    BookLoan toBookLoan(BookLoanRequest BookLoanRequest);
}

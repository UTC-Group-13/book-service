package org.example.bookservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.bookservice.entity.BookLoan;

import java.time.LocalDate;

@Data
public class NationalityRequest extends SearchRequest {

    private String nationality;
}
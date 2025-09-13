package org.example.bookservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// Payload for creating/updating a loan
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookLoanRequest {
    private Integer studentId;
    private Integer bookId;
    private Integer adminId;
    private LocalDate borrowDate; // optional; defaults to now if null
    private LocalDate dueDate;    // required on create
}
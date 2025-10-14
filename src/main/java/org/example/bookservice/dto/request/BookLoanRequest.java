package org.example.bookservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// Payload for creating/updating a loan
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookLoanRequest {
    @NotNull(message = "studentId là bắt buộc")
    private Integer studentId;

    @NotNull(message = "bookId là bắt buộc")
    private Integer bookId;

    @NotNull(message = "Ngày mượn là bắt buộc")
    private LocalDate borrowDate;

    @NotNull(message = "Ngày đến hạn là bắt buộc")
    private LocalDate dueDate;

    private BigDecimal fee = BigDecimal.ZERO;

    private Integer adminId = 1;

    private String status = "BORROWED";
}
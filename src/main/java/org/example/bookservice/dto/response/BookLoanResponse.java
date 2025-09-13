package org.example.bookservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookLoanResponse {
    private Integer id;
    private Integer studentId;
    private Integer bookId;
    private Integer adminId;

    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
    private BigDecimal fee;

    private Boolean deleteFlg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
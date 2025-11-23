package org.example.bookservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class OverdueDetailResponse {
    private LocalDate reportDate;
    private Long studentId;
    private Long bookId;
    private Long loanId;
    private LocalDate dueDate;
    private LocalDate borrowDate;
    private Integer daysOverdue;
    private Double estimatedFee;
}

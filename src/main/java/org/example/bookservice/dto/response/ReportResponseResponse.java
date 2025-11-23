package org.example.bookservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ReportResponseResponse {
    private LocalDate reportDate;
    private Integer totalBorrowed;
    private Integer totalOverdue;
    private Integer totalReturned;
    private Double totalFeeEstimate;
}

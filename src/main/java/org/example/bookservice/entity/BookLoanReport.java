package org.example.bookservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "BOOK_LOAN_REPORT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookLoanReport {

    @Id
    @Column(name = "REPORT_DATE")
    private LocalDate reportDate;

    @Column(name = "TOTAL_BORROWED")
    private Integer totalBorrowed;

    @Column(name = "TOTAL_OVERDUE")
    private Integer totalOverdue;

    @Column(name = "TOTAL_RETURNED")
    private Integer totalReturned;

    @Column(name = "TOTAL_FEE_ESTIMATE")
    private Double totalFeeEstimate;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
}


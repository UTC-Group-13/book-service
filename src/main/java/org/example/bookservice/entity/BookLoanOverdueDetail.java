package org.example.bookservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "BOOK_LOAN_OVERDUE_DETAIL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookLoanOverdueDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "REPORT_DATE")
    private LocalDate reportDate;

    @Column(name = "STUDENT_ID")
    private Long studentId;

    @Column(name = "BOOK_ID")
    private Long bookId;

    @Column(name = "LOAN_ID")
    private Long loanId;

    @Column(name = "DUE_DATE")
    private LocalDate dueDate;

    @Column(name = "BORROW_DATE")
    private LocalDate borrowDate;

    @Column(name = "DAYS_OVERDUE")
    private Integer daysOverdue;

    @Column(name = "ESTIMATED_FEE")
    private Double estimatedFee;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
}

package org.example.bookservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_loan_seq")
    @SequenceGenerator(
            name = "book_loan_seq",
            sequenceName = "BOOK_LOAN_SEQ",
            allocationSize = 1
    )
    private Integer id;

    @Column(name = "student_id", nullable = false)
    private Integer studentId;

    @Column(name = "book_id", nullable = false)
    private Integer bookId;

    @Column(name = "borrow_date")
    private LocalDate borrowDate = LocalDate.now();

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(length = 20)
    private String status = "BORROWED";

    @Column(name = "fee", precision = 10, scale = 2)
    private BigDecimal fee = BigDecimal.ZERO;

    @Column(name = "delete_flg")
    private Boolean deleteFlg = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "last_email_sent_date")
    private LocalDate lastEmailSentDate;
}

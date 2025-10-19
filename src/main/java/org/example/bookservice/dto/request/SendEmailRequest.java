package org.example.bookservice.dto.request;

import lombok.*;
import org.example.bookservice.entity.BookLoan;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendEmailRequest {
    private Long studentId;
    private Long bookId;
    private Long emailId;
    private LocalDate dueDate;
    private List<BookLoan> bookLoans;
    private Integer bookCount;
}

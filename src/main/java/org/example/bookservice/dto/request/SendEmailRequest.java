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
    private Integer studentId;
    private Integer bookId;
    private Integer emailId;
    private LocalDate dueDate;
    private List<BookLoan> bookLoans;
    private Integer bookCount;
}

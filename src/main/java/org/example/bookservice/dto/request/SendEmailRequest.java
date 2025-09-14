package org.example.bookservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendEmailRequest {
    @NotNull(message = "ID học sinh không được để trống")
    private Integer studentId;
    @NotNull(message = "ID sách không được để trống")
    private Integer bookId;
    private Integer emailId;
}

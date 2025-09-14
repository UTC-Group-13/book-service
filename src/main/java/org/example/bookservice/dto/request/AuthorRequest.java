package org.example.bookservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AuthorRequest {

    @NotNull(message = "Tên tác giả không được để trống")
    private String fullName;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate birthDate;

    @NotNull(message = "Quốc tịch không được để trống")
    private String nationality;

    @NotNull(message = "Tiểu sử không được để trống")
    private String biography;

    @NotNull(message = "Email không được để trống")
    private String email;
}
package org.example.bookservice.dto.request;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudentSearchRequest extends SearchRequest{

    private String search;

}
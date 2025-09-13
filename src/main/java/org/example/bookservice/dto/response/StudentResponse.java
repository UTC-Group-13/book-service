package org.example.bookservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {
    private Integer id;
    private String code;
    private String fullName;
    private String email;
    private String phone;
    private String departmentCode;
    private String classCode;

    private Boolean deleteFlg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
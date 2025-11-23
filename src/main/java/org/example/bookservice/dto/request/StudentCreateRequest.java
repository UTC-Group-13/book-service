package org.example.bookservice.dto.request;

import lombok.Data;

@Data
public class StudentCreateRequest {

    private String code;
    private String fullName;
    private String email;
    private String phone;
    private String departmentCode;
    private String classCode;
}
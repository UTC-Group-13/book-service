package org.example.bookservice.dto.request;

import lombok.Data;

@Data
public class StudentUpdateRequest {

    private String fullName;
    private String email;
    private String phone;
    private String departmentCode;
    private String classCode;
}

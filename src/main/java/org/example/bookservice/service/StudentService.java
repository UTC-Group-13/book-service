package org.example.bookservice.service;

import org.example.bookservice.dto.response.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {

    Page<StudentResponse> getAllStudents(
            String code,
            String fullName,
            String email,
            String phone,
            String departmentCode,
            String classCode,
            Pageable pageable
    );

    StudentResponse getStudentById(Integer id);
}
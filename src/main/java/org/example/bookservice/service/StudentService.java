package org.example.bookservice.service;

import org.example.bookservice.dto.request.StudentSearchRequest;
import org.example.bookservice.dto.response.StudentResponse;
import org.springframework.data.domain.Page;

public interface StudentService {

    Page<StudentResponse> getAllStudents(StudentSearchRequest request);

    StudentResponse getStudentById(Integer id);
}
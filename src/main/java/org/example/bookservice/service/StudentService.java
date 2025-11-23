package org.example.bookservice.service;

import org.example.bookservice.dto.request.StudentCreateRequest;
import org.example.bookservice.dto.request.StudentSearchRequest;
import org.example.bookservice.dto.request.StudentUpdateRequest;
import org.example.bookservice.dto.response.StudentResponse;
import org.example.bookservice.entity.Student;
import org.springframework.data.domain.Page;

public interface StudentService {

    Page<StudentResponse> getAllStudents(StudentSearchRequest request);

    StudentResponse getStudentById(Long id);

    StudentResponse create(StudentCreateRequest request);

    StudentResponse update(Long id, StudentUpdateRequest request);

    void delete(Long id);
}
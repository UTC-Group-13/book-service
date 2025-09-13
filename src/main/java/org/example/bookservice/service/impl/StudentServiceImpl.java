package org.example.bookservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.bookservice.dto.response.StudentResponse;
import org.example.bookservice.entity.Student;
import org.example.bookservice.repository.StudentRepository;
import org.example.bookservice.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public Page<StudentResponse> getAllStudents(
            String code,
            String fullName,
            String email,
            String phone,
            String departmentCode,
            String classCode,
            Pageable pageable
    ) {
        return studentRepository
                .findAllWithFilters(code, fullName, email, phone, departmentCode, classCode, pageable)
                .map(this::toResponse);
    }

    @Override
    public StudentResponse getStudentById(Integer id) {
        Student student = studentRepository.findById(id)
                .filter(s -> Boolean.FALSE.equals(s.getDeleteFlg()))
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        return toResponse(student);
    }

    private StudentResponse toResponse(Student s) {
        return StudentResponse.builder()
                .id(s.getId())
                .code(s.getCode())
                .fullName(s.getFullName())
                .email(s.getEmail())
                .phone(s.getPhone())
                .departmentCode(s.getDepartmentCode())
                .classCode(s.getClassCode())
                .deleteFlg(s.getDeleteFlg())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}
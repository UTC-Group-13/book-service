package org.example.bookservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.bookservice.dto.response.StudentResponse;
import org.example.bookservice.entity.Student;
import org.example.bookservice.mapper.StudentMapper;
import org.example.bookservice.repository.StudentRepository;
import org.example.bookservice.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

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
                .map(studentMapper::toStudentResponse);
    }

    @Override
    public StudentResponse getStudentById(Integer id) {
        Student student = studentRepository.findById(id)
                .filter(s -> Boolean.FALSE.equals(s.getDeleteFlg()))
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        return studentMapper.toStudentResponse(student);
    }
}
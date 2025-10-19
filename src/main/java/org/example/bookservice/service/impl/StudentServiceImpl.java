package org.example.bookservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.bookservice.dto.request.StudentSearchRequest;
import org.example.bookservice.dto.response.StudentResponse;
import org.example.bookservice.entity.Student;
import org.example.bookservice.mapper.StudentMapper;
import org.example.bookservice.repository.StudentRepository;
import org.example.bookservice.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public Page<StudentResponse> getAllStudents(StudentSearchRequest request) {
        Sort sort = Sort.unsorted();
        if (request.getSortBy() != null) {
            sort = Sort.by(
                    "DESC".equalsIgnoreCase(request.getSortDir()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                    request.getSortBy()
            );
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        return studentRepository
                .findAllWithFilters(request.getSearch(), pageable)
                .map(studentMapper::toStudentResponse);
    }

    @Override
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .filter(s -> Boolean.FALSE.equals(s.getDeleteFlg()))
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        return studentMapper.toStudentResponse(student);
    }

}
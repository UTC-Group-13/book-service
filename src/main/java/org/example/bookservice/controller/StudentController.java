package org.example.bookservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.bookservice.dto.response.StudentResponse;
import org.example.bookservice.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@AllArgsConstructor
@Tag(name = "Student API", description = "Read and search students")
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "Get all students", description = "Returns a paginated list of students filtered by optional fields")
    @GetMapping
    public ResponseEntity<Page<StudentResponse>> getAllStudents(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String departmentCode,
            @RequestParam(required = false) String classCode,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                studentService.getAllStudents(code, fullName, email, phone, departmentCode, classCode, pageable)
        );
    }

    @Operation(summary = "Get a student by ID", description = "Fetch details of a single student by ID")
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Integer id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }
}
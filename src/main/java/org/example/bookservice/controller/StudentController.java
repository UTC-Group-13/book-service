package org.example.bookservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.bookservice.dto.request.StudentSearchRequest;
import org.example.bookservice.dto.response.StudentResponse;
import org.example.bookservice.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@AllArgsConstructor
@Tag(name = "Student API", description = "Read and search students")
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "Get all students", description = "Returns a paginated list of students filtered by optional fields fullName, code, email, departmentCode, classCode")
    @PostMapping("/search")
    public ResponseEntity<Page<StudentResponse>> getAllStudents(@RequestBody StudentSearchRequest request) {
        return ResponseEntity.ok(
                studentService.getAllStudents(request)
        );
    }

    @Operation(summary = "Get a student by ID", description = "Fetch details of a single student by ID")
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Integer id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }
}
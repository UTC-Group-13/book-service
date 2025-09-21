package org.example.bookservice.mapper;

import org.example.bookservice.dto.response.StudentResponse;
import org.example.bookservice.entity.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // để Spring quản lý
public interface StudentMapper {
    StudentResponse toStudentResponse(Student Student);

}


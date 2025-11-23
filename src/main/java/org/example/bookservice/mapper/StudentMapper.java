package org.example.bookservice.mapper;

import org.example.bookservice.dto.request.StudentCreateRequest;
import org.example.bookservice.dto.request.StudentUpdateRequest;
import org.example.bookservice.dto.response.StudentResponse;
import org.example.bookservice.entity.Student;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring") // để Spring quản lý
public interface StudentMapper {
    StudentResponse toStudentResponse(Student student);

    Student toEntity(StudentCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Student entity, StudentUpdateRequest request);

    StudentResponse toResponse(Student entity);

}


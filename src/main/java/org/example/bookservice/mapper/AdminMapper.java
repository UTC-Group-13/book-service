package org.example.bookservice.mapper;

import org.example.bookservice.dto.response.AdminResponse;
import org.example.bookservice.entity.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // để Spring quản lý
public interface AdminMapper {
    AdminResponse toAdminResponse(Admin admin);
}

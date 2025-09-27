package org.example.bookservice.mapper;

import org.example.bookservice.dto.response.NationalityResponse;
import org.example.bookservice.entity.Nationality;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NationalityMapper {
    List<NationalityResponse> toResponseList(List<Nationality> nationalities);
}

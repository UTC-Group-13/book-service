package org.example.bookservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookservice.dto.response.NationalityResponse;
import org.example.bookservice.mapper.NationalityMapper;
import org.example.bookservice.repository.NationalityRepository;
import org.example.bookservice.service.NationalityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NationalityServiceImpl implements NationalityService {
    private final NationalityRepository nationalityRepository;
    private final NationalityMapper nationalityMapper;

    @Override
    public List<NationalityResponse> findAll() {
        return nationalityMapper.toResponseList(nationalityRepository.findAll());
    }
}

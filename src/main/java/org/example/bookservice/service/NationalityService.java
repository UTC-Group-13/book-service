package org.example.bookservice.service;

import org.example.bookservice.dto.response.NationalityResponse;

import java.util.List;

public interface NationalityService {
    List<NationalityResponse> findAll();
}

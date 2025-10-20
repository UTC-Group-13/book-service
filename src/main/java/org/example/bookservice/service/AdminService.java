package org.example.bookservice.service;

import org.example.bookservice.dto.request.RegisterRequest;
import org.example.bookservice.dto.response.AdminResponse;

public interface AdminService {
    AdminResponse createAdmin(RegisterRequest request);
}

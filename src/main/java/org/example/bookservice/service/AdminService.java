package org.example.bookservice.service;

import org.example.bookservice.dto.request.RegisterRequest;
import org.example.bookservice.dto.response.AdminResponse;
import org.example.bookservice.dto.response.AuthInfoResponse;

public interface AdminService {
    AdminResponse createAdmin(RegisterRequest request);

    AuthInfoResponse getInfo(String username);
}

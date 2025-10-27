package org.example.bookservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookservice.constant.ErrorCode;
import org.example.bookservice.dto.exception.BusinessException;
import org.example.bookservice.dto.request.RegisterRequest;
import org.example.bookservice.dto.response.AdminResponse;
import org.example.bookservice.dto.response.AuthInfoResponse;
import org.example.bookservice.entity.Admin;
import org.example.bookservice.mapper.AdminMapper;
import org.example.bookservice.repository.AdminRepository;
import org.example.bookservice.service.AdminService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminMapper adminMapper;

    @Override
    public AdminResponse createAdmin(RegisterRequest request) {
        if (adminRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BusinessException(ErrorCode.ADMIN_ALREADY_EXISTS.getCode(), ErrorCode.ADMIN_ALREADY_EXISTS.getMessage());
        }

        Admin admin = Admin.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .role("STAFF")
                .build();

        return adminMapper.toAdminResponse(adminRepository.save(admin));
    }

    @Override
    public AuthInfoResponse getInfo(String username) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.ADMIN_NOT_EXISTS.getCode(), ErrorCode.ADMIN_NOT_EXISTS.getMessage()));
        return adminMapper.toAuthInfoResponse(admin);
    }
}

package org.example.bookservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.bookservice.configuration.JwtUtils;
import org.example.bookservice.constant.ErrorCode;
import org.example.bookservice.dto.exception.BusinessException;
import org.example.bookservice.dto.response.AuthInfoResponse;
import org.example.bookservice.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin API", description = "Manage admin")
public class AdminController {

    private final AdminService adminService;
    private final JwtUtils jwtUtils;

    @Operation(summary = "Get info", description = "Info")
    @PostMapping("/info")
    public ResponseEntity<AuthInfoResponse> getInfo(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException(ErrorCode.TOKEN_NOT_FOUND.getMessage(), ErrorCode.TOKEN_NOT_FOUND.getCode());
        }

        String token = authHeader.substring(7);

        String username = jwtUtils.extractUsername(token);
        return ResponseEntity.ok(adminService.getInfo(username));

    }
}


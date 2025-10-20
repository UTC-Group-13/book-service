package org.example.bookservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookservice.configuration.JwtUtils;
import org.example.bookservice.dto.*;
import org.example.bookservice.dto.request.LoginRequest;
import org.example.bookservice.dto.request.RegisterRequest;
import org.example.bookservice.dto.response.AdminResponse;
import org.example.bookservice.dto.response.AuthResponse;
import org.example.bookservice.dto.response.AuthorResponse;
import org.example.bookservice.entity.Admin;
import org.example.bookservice.repository.AdminRepository;
import org.example.bookservice.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AdminResponse> register(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(adminService.createAdmin(req));
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        String token = jwtUtils.generateToken(req.getUsername());
        return new AuthResponse(token);
    }
}


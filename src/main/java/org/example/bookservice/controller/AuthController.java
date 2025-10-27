package org.example.bookservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.bookservice.configuration.JwtUtils;
import org.example.bookservice.constant.ErrorCode;
import org.example.bookservice.dto.exception.BusinessException;
import org.example.bookservice.dto.request.LoginRequest;
import org.example.bookservice.dto.request.RegisterRequest;
import org.example.bookservice.dto.response.AdminResponse;
import org.example.bookservice.dto.response.AuthInfoResponse;
import org.example.bookservice.dto.response.AuthResponse;
import org.example.bookservice.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API", description = "Manage auth")
public class AuthController {

    private final AdminService adminService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Create an admin", description = "Creates a new admin")
    @PostMapping("/register")
    public ResponseEntity<AdminResponse> register(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(adminService.createAdmin(req));
    }

    @Operation(summary = "Login account admin", description = "Login account admin")
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        String token = jwtUtils.generateToken(req.getUsername());
        long expiresAt = jwtUtils.getExpirationTime(token);
        return new AuthResponse(token, expiresAt);
    }

    @Operation(summary = "Refresh token", description = "Refresh token")
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
        }

        String oldToken = authHeader.substring(7);

        // Kiểm tra token có hợp lệ không
        try {
            if (jwtUtils.isTokenExpired(oldToken)) {
                return ResponseEntity.status(401).body("Token expired. Please login again.");
            }

            // Sinh token mới (7 ngày)
            String newToken = jwtUtils.refreshToken(oldToken);
            long expiresAt = jwtUtils.getExpirationTime(newToken);
            return ResponseEntity.ok(new AuthResponse(newToken, expiresAt));

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token: " + e.getMessage());
        }
    }
}


package org.example.bookservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookservice.dto.request.SendEmailRequest;
import org.example.bookservice.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/send-email")
@RequiredArgsConstructor
@Tag(name = "Email API")
public class SendEmailController {

    private final EmailService emailService;

    @Operation(summary = "Send email", description = "Send email when a student borrows a book successfully")
    @PostMapping
    public ResponseEntity<Object> getAllAuthors(@Valid @RequestBody SendEmailRequest request) {
        emailService.sendEmail(request);
        return ResponseEntity.ok("Send Email Success!");
    }
}
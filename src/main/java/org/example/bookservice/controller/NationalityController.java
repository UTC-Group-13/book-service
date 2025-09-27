package org.example.bookservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.bookservice.dto.response.NationalityResponse;
import org.example.bookservice.service.NationalityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/nationality")
@RequiredArgsConstructor
@Tag(name = "Nationality API", description = "Manage nationality through CRUD + search API endpoints")
public class NationalityController {
    private final NationalityService nationalityService;

    @Operation(summary = "Get all authors", description = "Returns all nationality")
    @GetMapping("/getAll")
    public ResponseEntity<List<NationalityResponse>> getAllNationality() {
        return ResponseEntity.ok(nationalityService.findAll());
    }
}

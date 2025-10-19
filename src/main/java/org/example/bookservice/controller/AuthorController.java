package org.example.bookservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookservice.dto.request.AuthorRequest;
import org.example.bookservice.dto.request.AuthorSearchRequest;
import org.example.bookservice.dto.response.AuthorResponse;
import org.example.bookservice.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Tag(name = "Author API", description = "Manage authors through CRUD + search API endpoints")
public class AuthorController {

    private final AuthorService authorService;

    @Operation(summary = "Get all authors", description = "Returns a paginated list of authors filtered by optional name, nationality, and email")
    @PostMapping("/search")
    public ResponseEntity<Page<AuthorResponse>> getAllAuthors(@RequestBody AuthorSearchRequest request) {
        return ResponseEntity.ok(authorService.getAllAuthors(request));
    }

    @Operation(summary = "Create an author", description = "Creates a new author")
    @PostMapping
    public ResponseEntity<AuthorResponse> createAuthor(@Valid @RequestBody AuthorRequest request) {
        AuthorResponse created = authorService.createAuthor(request);
        return ResponseEntity.status(201).body(created);
    }

    @Operation(summary = "Get an author by ID", description = "Fetch details of a single author by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @Operation(summary = "Update an author", description = "Updates details of an existing author")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorRequest request) {
        return ResponseEntity.ok(authorService.updateAuthor(id, request));
    }

    @Operation(summary = "Delete an author", description = "Soft deletes an author by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
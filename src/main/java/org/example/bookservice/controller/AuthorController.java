package org.example.bookservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.bookservice.dto.request.AuthorRequest;
import org.example.bookservice.dto.response.AuthorResponse;
import org.example.bookservice.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Tag(name = "Author API", description = "Manage authors through CRUD + search API endpoints")
public class AuthorController {

    private final AuthorService authorService;

    @Operation(summary = "Get all authors", description = "Returns a paginated list of authors filtered by optional name, nationality, and email")
    @GetMapping
    public ResponseEntity<Page<AuthorResponse>> getAllAuthors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String nationality,
            @RequestParam(required = false) String email,
            Pageable pageable
    ) {
        return ResponseEntity.ok(authorService.getAllAuthors(name, nationality, email, pageable));
    }

    @Operation(summary = "Create an author", description = "Creates a new author")
    @PostMapping
    public ResponseEntity<AuthorResponse> createAuthor(@RequestBody AuthorRequest request) {
        AuthorResponse created = authorService.createAuthor(request);
        return ResponseEntity.status(201).body(created);
    }

    @Operation(summary = "Get an author by ID", description = "Fetch details of a single author by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable Integer id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @Operation(summary = "Update an author", description = "Updates details of an existing author")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> updateAuthor(@PathVariable Integer id, @RequestBody AuthorRequest request) {
        return ResponseEntity.ok(authorService.updateAuthor(id, request));
    }

    @Operation(summary = "Delete an author", description = "Soft deletes an author by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Integer id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
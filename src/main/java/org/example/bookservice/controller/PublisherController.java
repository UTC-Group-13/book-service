package org.example.bookservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.bookservice.dto.request.PublisherRequest;
import org.example.bookservice.dto.response.PublisherResponse;
import org.example.bookservice.service.PublisherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/publishers")
@AllArgsConstructor
@Tag(name = "Publisher API", description = "Manage publishers through CRUD + search API endpoints")
public class PublisherController {

    private final PublisherService publisherService;

    @Operation(summary = "Get all publishers", description = "Returns a paginated list of publishers filtered by optional name")
    @GetMapping
    public ResponseEntity<Page<PublisherResponse>> getAllPublishers(
            @RequestParam(required = false) String name,
            Pageable pageable
    ) {
        return ResponseEntity.ok(publisherService.getAllPublishers(name, pageable));
    }

    @Operation(summary = "Create a publisher", description = "Creates a new publisher")
    @PostMapping
    public ResponseEntity<PublisherResponse> createPublisher(@RequestBody PublisherRequest request) {
        PublisherResponse created = publisherService.createPublisher(request);
        return ResponseEntity.status(201).body(created);
    }

    @Operation(summary = "Get a publisher by ID", description = "Fetch details of a single publisher by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponse> getPublisherById(@PathVariable Integer id) {
        return ResponseEntity.ok(publisherService.getPublisherById(id));
    }

    @Operation(summary = "Update a publisher", description = "Updates details of an existing publisher")
    @PutMapping("/{id}")
    public ResponseEntity<PublisherResponse> updatePublisher(@PathVariable Integer id, @RequestBody PublisherRequest request) {
        return ResponseEntity.ok(publisherService.updatePublisher(id, request));
    }

    @Operation(summary = "Delete a publisher", description = "Soft deletes a publisher by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Integer id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }
}
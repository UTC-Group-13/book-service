package org.example.bookservice.service;

import org.example.bookservice.dto.request.PublisherRequest;
import org.example.bookservice.dto.request.PublisherSearchRequest;
import org.example.bookservice.dto.response.PublisherResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PublisherService {
    Page<PublisherResponse> getAllPublishers(PublisherSearchRequest request);
    PublisherResponse createPublisher(PublisherRequest request);
    PublisherResponse getPublisherById(Long id);
    PublisherResponse updatePublisher(Long id, PublisherRequest request);
    void deletePublisher(Long id);
}
package org.example.bookservice.service;

import org.example.bookservice.dto.request.PublisherRequest;
import org.example.bookservice.dto.response.PublisherResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PublisherService {
    Page<PublisherResponse> getAllPublishers(String name, Pageable pageable);
    PublisherResponse createPublisher(PublisherRequest request);
    PublisherResponse getPublisherById(Integer id);
    PublisherResponse updatePublisher(Integer id, PublisherRequest request);
    void deletePublisher(Integer id);
}
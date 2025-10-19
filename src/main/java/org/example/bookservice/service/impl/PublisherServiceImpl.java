package org.example.bookservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.bookservice.dto.request.PublisherRequest;
import org.example.bookservice.dto.request.PublisherSearchRequest;
import org.example.bookservice.dto.response.PublisherResponse;
import org.example.bookservice.entity.Publisher;
import org.example.bookservice.mapper.PublisherMapper;
import org.example.bookservice.repository.PublisherRepository;
import org.example.bookservice.service.PublisherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    @Override
    public Page<PublisherResponse> getAllPublishers(PublisherSearchRequest request) {
        Sort sort = Sort.unsorted();
        if (request.getSortBy() != null) {
            sort = Sort.by(
                    "DESC".equalsIgnoreCase(request.getSortDir()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                    request.getSortBy()
            );
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        return publisherRepository
                .findAllWithFilters(request.getSearch(), pageable)
                .map(publisherMapper::toPublisherResponse );
    }

    @Override
    public PublisherResponse createPublisher(PublisherRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Publisher name is required");
        }
        if (publisherRepository.existsByNameIgnoreCase(request.getName().trim())) {
            throw new IllegalArgumentException("Publisher name already exists");
        }

        Publisher entity = Publisher.builder()
                .name(request.getName().trim())
                .description(request.getDescription())
                .deleteFlg(false)
                .build();

        return publisherMapper.toPublisherResponse(publisherRepository.save(entity));
    }

    @Override
    public PublisherResponse getPublisherById(Long id) {
        Publisher entity = publisherRepository.findById(id)
                .filter(p -> Boolean.FALSE.equals(p.getDeleteFlg()))
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found"));
        return publisherMapper.toPublisherResponse(entity);
    }

    @Override
    public PublisherResponse updatePublisher(Long id, PublisherRequest request) {
        Publisher entity = publisherRepository.findById(id)
                .filter(p -> Boolean.FALSE.equals(p.getDeleteFlg()))
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found"));

        if (request.getName() != null) {
            String newName = request.getName().trim();
            if (newName.isEmpty()) {
                throw new IllegalArgumentException("Publisher name cannot be empty");
            }
            if (!newName.equalsIgnoreCase(entity.getName())
                    && publisherRepository.existsByNameIgnoreCase(newName)) {
                throw new IllegalArgumentException("Publisher name already exists");
            }
            entity.setName(newName);
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }

        return publisherMapper.toPublisherResponse(publisherRepository.save(entity));
    }

    @Override
    public void deletePublisher(Long id) {
        Publisher entity = publisherRepository.findById(id)
                .filter(p -> Boolean.FALSE.equals(p.getDeleteFlg()))
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found"));

        entity.setDeleteFlg(true); // soft delete
        publisherRepository.save(entity);
    }
}
package org.example.bookservice.mapper;

import org.example.bookservice.dto.request.PublisherRequest;
import org.example.bookservice.dto.response.PublisherResponse;
import org.example.bookservice.entity.Publisher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // để Spring quản lý
public interface PublisherMapper {
    PublisherResponse toPublisherResponse(Publisher publisher);

    Publisher toPublisher(PublisherRequest publisherRequest);
}


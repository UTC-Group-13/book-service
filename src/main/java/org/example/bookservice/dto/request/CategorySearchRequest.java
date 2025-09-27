package org.example.bookservice.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategorySearchRequest extends SearchRequest{

    private String search;
}
package org.example.bookservice.constant;

import lombok.Getter;

@Getter
public enum Status {
    SUCCESS(1), FAILURE(2), PENDING(3);

    private final Integer value;

    Status(Integer value) {
        this.value = value;
    }
}

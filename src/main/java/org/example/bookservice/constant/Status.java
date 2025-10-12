package org.example.bookservice.constant;

import lombok.Getter;

@Getter
public enum Status {
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE"),
    PENDING("PENDING"),
    BORROWING("BORROWING"),
    RETURNED("RETURNED"),
    LATE("LATE")
    ;

    private final String value;

    Status(String value) {
        this.value = value;
    }
}

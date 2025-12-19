package org.example.bookservice.dto.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException{

    private final String code;

    public BusinessException(String message, String code) {
        super(message);
        this.code = code;
    }


}

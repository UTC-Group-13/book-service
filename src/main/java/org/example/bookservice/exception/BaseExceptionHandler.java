package org.example.bookservice.exception;

import org.example.bookservice.dto.exception.BusinessException;
import org.example.bookservice.dto.exception.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusiness(BusinessException ex) {
        return ResponseEntity.badRequest().body(ResponseError.toError(ex.getCode(), ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
}

package org.example.bookservice.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseError {
    private String code;
    private Integer status;
    private LocalDateTime timeStamp;
    private Object data;


    public static ResponseError toError(String code, Object data, Integer status){
        return  ResponseError.builder()
                .timeStamp(LocalDateTime.now())
                .status(status)
                .code(code)
                .data(data)
                .build();
    }
}

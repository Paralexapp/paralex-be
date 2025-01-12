package com.paralex.erp.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Getter
@Setter
public class GlobalResponse<T> {
    private String message;
    private HttpStatus status;
    private T data;
    private String debugMessage;
    private String token;
    private String dateTime = LocalDateTime.now().toString();

    public GlobalResponse(T httpStatus, T customerNotFound) {
    }
}



package com.paralex.erp.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
@RequiredArgsConstructor
public class OkResponse<T> {
    private HttpStatusCode statusCode;
    private String response;
    private String status;
    private T data;
    private String dateTime;
    private String debugMessage;
}
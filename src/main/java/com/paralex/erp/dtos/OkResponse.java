package com.paralex.erp.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class OkResponse<T> {
    private String statusCode;
    private String response;
    private String status;
    private T data;
    private String dateTime;
    private String debugMessage;
}
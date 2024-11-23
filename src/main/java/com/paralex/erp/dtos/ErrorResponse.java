package com.paralex.erp.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String status;
    private String message;
    private Object data;

    public ErrorResponse(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}

package com.paralex.erp.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class NewOkResponse<T> {
    private HttpStatus statusCode;
    private String response;
    private String status;
    private List<T> data;
    private String dateTime;
    private String debugMessage;

    public NewOkResponse(HttpStatus statusCode, String response, String status, List<T> data) {
        this.statusCode = statusCode;
        this.response = response;
        this.status = status;
        this.data = data;
        this.dateTime = java.time.LocalDateTime.now().toString();
        this.debugMessage = "Request completed successfully";
    }
}

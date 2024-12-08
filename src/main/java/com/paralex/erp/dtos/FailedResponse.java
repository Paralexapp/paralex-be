package com.paralex.erp.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class FailedResponse {
    private HttpStatusCode status;
    private String response;
    private String debugMessage;
    private String dateTime;

    @Override
    public String toString() {
        return "FailedResponse{" +
                "status=" + status +
                ", response='" + response + '\'' +
                ", debugMessage='" + debugMessage + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
package com.paralex.erp.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class TransactionResponseDto {
    private boolean status;
    private String message;
    private Data data;

    @Getter
    @Setter
    public static class Data {
        private String authorization_url;
        private String access_code;
        private String reference;
    }
}
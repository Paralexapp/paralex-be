package com.paralex.erp.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class InitTransactionDto {
    private String email;
    private String amount;
    private List<String> channels;

    public InitTransactionDto() {
        this.channels = Arrays.asList("card", "bank_transfer");
    }
}

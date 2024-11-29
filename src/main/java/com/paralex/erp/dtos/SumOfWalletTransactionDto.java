package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SumOfWalletTransactionDto {
    @NotNull
    private int sum;
}

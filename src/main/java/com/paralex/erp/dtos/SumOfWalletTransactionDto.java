package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SumOfWalletTransactionDto {
    @NotNull
    private int sum;
}

package com.paralex.erp.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class WalletBalanceDto {
    @NonNull
    private Integer balance;
}

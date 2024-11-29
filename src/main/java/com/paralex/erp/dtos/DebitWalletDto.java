package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DebitWalletDto {
    @NotNull
    // INFO the amount should never be so expensive that it is a long
    private int amount;
}

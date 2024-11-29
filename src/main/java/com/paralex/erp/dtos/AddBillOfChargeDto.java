package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AddBillOfChargeDto {
    @NonNull
    @NotEmpty
    @NotBlank
    private String name;

    @NotNull
    private int amount;

    @NonNull
    @NotEmpty
    @NotBlank
    private String litigationSupportRequestId;
}

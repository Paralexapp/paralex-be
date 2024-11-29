package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NonNull;

@Data
public class SetLitigationSupportFileNumberDto {
    @NonNull
    @NotEmpty
    @NotBlank
    private String fileNumber;
}

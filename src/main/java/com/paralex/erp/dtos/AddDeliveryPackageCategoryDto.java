package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddDeliveryPackageCategoryDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @Nullable
    private String description;

    @Nullable
    private Boolean status;
}

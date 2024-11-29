package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class UpdateDeliveryPackageCategoryDto {
    @Nullable
    private String name;

    @Nullable
    private String description;

    @Nullable
    private Boolean status;
}

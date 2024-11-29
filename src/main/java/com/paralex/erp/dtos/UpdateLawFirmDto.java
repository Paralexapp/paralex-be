package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class UpdateLawFirmDto {
    @Nullable
    private String name;
}

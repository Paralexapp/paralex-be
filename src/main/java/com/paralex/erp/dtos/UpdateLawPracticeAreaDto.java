package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class UpdateLawPracticeAreaDto {
    @Nullable
    private String name;

    @Nullable
    private Boolean status;
}

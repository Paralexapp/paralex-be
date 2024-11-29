package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class StatesByCountryDto {
    @Nullable
    private String countryCode;

    @Nullable
    private String iso2;

    @Nullable
    private String countryId;
}

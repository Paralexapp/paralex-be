package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddLawFirmPracticeAreaDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String lawPracticeAreaId;

    @NotNull
    @NotEmpty
    @NotBlank
    private String lawFirmId;
}

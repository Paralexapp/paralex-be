package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateDeliveryStageDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @NotNull
    private boolean initial;

    @NotNull
    private boolean terminal;

    @NotNull
    private boolean status;

    @NotNull
    private boolean forDriver;

    @NotNull
    private boolean forAdmin;

    @NotNull
    private boolean shouldNotify;
}

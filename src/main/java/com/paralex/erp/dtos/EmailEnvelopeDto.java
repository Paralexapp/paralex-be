package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class EmailEnvelopeDto {
    @NotNull
    private String emailBody;

    @NotNull
    private String toAddress;

    @NotNull
    private String subject;

    @NotNull
    private String fromAddress;

    @Nullable
    private List<String> ccs;

    @Nullable
    private List<String> bcs;
}

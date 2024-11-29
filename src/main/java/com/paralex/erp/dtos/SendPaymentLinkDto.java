package com.paralex.erp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SendPaymentLinkDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String customer;

    @NotNull
    private double amount;

    @NotNull
    private String description;

    @Builder.Default
    @NotNull
    @NotEmpty
    @JsonProperty(value = "line_items")
    private List<LineItemDto> lineItems = List.of();

    @Builder.Default
    @NotNull
    @NotEmpty
    private List<TaxDto> tax = List.of();

    @Nullable
    @JsonProperty(value = "split_code")
    private String splitCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class LineItemDto extends TaxDto {

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class TaxDto {
        @NotNull
        @NotEmpty
        @NotBlank
        private String name;

        @NotNull
        private double amount;
    }
}

package com.paralex.erp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyTransactionByReferenceResponseDto {
    @NotNull
    private long id;

    @NotNull
    @NotEmpty
    @NotBlank
    private String domain;

    @NotNull
    @NotEmpty
    @NotBlank
    private String status;

    @NotNull
    @NotEmpty
    @NotBlank
    private String reference;

    @Nullable
    @JsonProperty(value = "receipt_number")
    private Object receiptNumber;

    @NotNull
    private int amount;

    @Nullable
    private Object message;

    @NotNull
    @NotEmpty
    @NotBlank
    @JsonProperty(value = "gateway_response")
    private String gatewayResponse;

    @NotNull
    @JsonProperty(value = "paid_at")
    @DateTimeFormat(iso = DATE_TIME)
    private LocalDateTime paidAt;

    @NotNull
    @JsonProperty(value = "created_at")
    @DateTimeFormat(iso = DATE_TIME)
    private LocalDateTime createdAt;

    @NotNull
    @NotEmpty
    @NotBlank
    private String channel;

    @NotNull
    @NotEmpty
    @NotBlank
    private String currency;

    @NotNull
    @NotEmpty
    @NotBlank
    @JsonProperty(value = "ip_address")
    private String ipAddress;

    @NotNull
    @NotEmpty
    @NotBlank
    private String metadata;

    @NotNull
    private Log log;

    @NotNull
    private int fees;

    @Nullable
    @JsonProperty(value = "fees_split")
    private Object feesSplit;

    @NotNull
    private Authorization authorization;

    @NotNull
    private Customer customer;

    @NotNull
    private Object plan;

    @NotNull
    private Split split;

    @Nullable
    @JsonProperty(value = "order_id")
    private Object orderId;

    @NotNull
    @JsonProperty(value = "paidAt")
    @DateTimeFormat(iso = DATE_TIME)
    private LocalDateTime secondPaidAt;

    @NotNull
    @JsonProperty(value = "createdAt")
    @DateTimeFormat(iso = DATE_TIME)
    private LocalDateTime secondCreatedAt;

    @NotNull
    @JsonProperty(value = "requested_amount")
    private int requestedAmount;

    @Nullable
    @JsonProperty(value = "pos_transaction_data")
    private Object posTransactionData;

    @Nullable
    private Object source;

    @NotNull
    @JsonProperty(value = "fees_breakdown")
    private Object feesBreakdown;

    @Nullable
    private Object connect;

    @NotNull
    @JsonProperty(value = "transaction_date")
    @DateTimeFormat(iso = DATE_TIME)
    private LocalDateTime transactionDate;

    @NotNull
    @JsonProperty(value = "plan_object")
    private PlanObject planObject;

    @NotNull
    private VerifyTransactionByReferenceResponseDto.SubAccount subaccount;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Authorization {
        @NotNull
        @NotEmpty
        @NotBlank
        @JsonProperty(value = "authorizationCode")
        private String authorizationCode;

        @NotNull
        @NotEmpty
        @NotBlank
        private String bin;

        @NotNull
        @NotEmpty
        @NotBlank
        private String last4;

        @NotNull
        @NotEmpty
        @NotBlank
        @JsonProperty(value = "exp_month")
        private String expMonth;

        @NotNull
        @NotEmpty
        @NotBlank
        @JsonProperty(value = "exp_year")
        private String expYear;

        @NotNull
        @NotEmpty
        @NotBlank
        private String channel;

        @NotNull
        @NotEmpty
        @NotBlank
        @JsonProperty(value = "card_type")
        private String cardType;

        @NotNull
        @NotEmpty
        @NotBlank
        private String bank;

        @NotNull
        @NotEmpty
        @NotBlank
        @JsonProperty(value = "country_code")
        private String countryCode;

        @NotNull
        @NotEmpty
        @NotBlank
        private String brand;

        @NotNull
        private boolean reusable;

        @NotNull
        @NotEmpty
        @NotBlank
        private String signature;

        @Nullable
        @JsonProperty(value = "account_name")
        private Object accountName;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Customer {
        @NotNull
        private int id;

        @NotNull
        @JsonProperty(value = "first_name")
        private String firstName;

        @NotNull
        @JsonProperty(value = "last_name")
        private String lastName;

        @NotNull
        @NotEmpty
        @NotBlank
        private String email;

        @NotNull
        @NotEmpty
        @NotBlank
        @JsonProperty(value = "customer_code")
        private String customerCode;

        @NotNull
        private String phone;

        @Nullable
        private Object metadata;

        @NotNull
        @NotEmpty
        @NotBlank
        @JsonProperty(value = "risk_action")
        private String riskAction;

        @Nullable
        @JsonProperty(value = "international_format_phone")
        private Object internationalFormatPhone;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class History {
        @NotNull
        @NotEmpty
        @NotBlank
        private String type;

        @NotNull
        @NotEmpty
        @NotBlank
        private String message;

        @NotNull
        private int time;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Log {
        @NotNull
        @JsonProperty(value = "start_time")
        private int startTime;

        @NotNull
        @JsonProperty(value = "time_spent")
        private int timeSpent;

        @NotNull
        private int attempts;

        @NotNull
        private int errors;

        @NotNull
        private boolean success;

        @NotNull
        private boolean mobile;

        @NotNull
        private List<Object> input = List.of();

        @NotNull
        private List<History> history = List.of();
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class PlanObject {
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Split {
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class SubAccount {
    }
}

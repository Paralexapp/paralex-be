package com.paralex.erp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude()
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PayStackWebhookDataDto {
    @NotNull
    @NotEmpty
    @NotBlank
    @JsonProperty("event")
    private String event;

    @NotNull
    @JsonInclude()
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonProperty("data")
    private WebhookData data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude()
    @Data
    public static class WebhookData {
        @NotNull
        @JsonProperty("id")
        private int id;

        @NotNull
        @NotEmpty
        @NotBlank
        @JsonProperty("domain")
        private String domain;

        @NotNull
        @JsonProperty("amount")
        private int amount;

        @NotNull
        @NotEmpty
        @NotBlank
        @JsonProperty("currency")
        private String currency;

        @Nullable
        @JsonProperty("due_date")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate dueDate;

        @NotNull
        @JsonProperty("has_invoice")
        private boolean hasInvoice;

        @Nullable
        @JsonProperty("invoice_number")
        private Integer invoiceNumber;

        // INFO because of others that may not have a description for some reason
        @Nullable
        @JsonProperty("description")
        private String description;

        @Nullable
        @JsonProperty("pdf_url")
        private String pdfUrl;

        // INFO because of other payment types e.g wallet transactions (but are not considered for webhook, anyway), they should be nullable
        @Nullable
        @JsonProperty("line_items")
        private List<SendPaymentLinkDto.LineItemDto> lineItems = List.of();

        @Nullable
        @JsonProperty("tax")
        private List<Object> tax = List.of();

        @Nullable
        @JsonProperty("request_code")
        private String requestCode;

        @Nullable
        @JsonProperty("status")
        private String status;

        @NotNull
        @JsonProperty("paid")
        private boolean paid;

        @NotNull
        @JsonProperty("paid_at")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDate paidAt;

        @Nullable
        @JsonProperty("metadata")
        private MetaData metadata;

        @Nullable
        @JsonProperty("notifications")
        private List<Notification> notifications = List.of();

        @Nullable
        @JsonProperty("offline_reference")
        private String offlineReference;

        // INFO THIS IS different for other events, it is a potential trouble
        //@NotNull
        //@JsonProperty("customer")
        //private int customer;

        @NotNull
        @JsonProperty("created_at")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime createdAt;
    }

    // INFO not required for payment links but required for wallet though
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    @Data
    public static class MetaData {
        // INFO other fields when required

        @NotNull
        @JsonProperty("custom_fields")
        private List<CustomField> customFields = List.of();
    }

    @JsonIgnoreProperties
    @JsonInclude(JsonInclude.Include.ALWAYS)
    @Data
    public static class CustomField {
        @NotNull
        @NotEmpty
        @NotBlank
        @JsonProperty("display_name")
        private String displayName;

        @NotNull
        @NotEmpty
        @NotBlank
        @JsonProperty("variable_name")
        private String variableName;

        @NotNull
        private int value;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude()
    @Data
    public static class Notification {
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @JsonProperty("sent_at")
        private LocalDateTime sentAt;

        @NotNull
        @NotEmpty
        @NotBlank
        @JsonProperty("channel")
        private String channel;
    }
}

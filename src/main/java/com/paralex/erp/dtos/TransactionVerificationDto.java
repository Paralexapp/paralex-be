package com.paralex.erp.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Setter
public class TransactionVerificationDto {

    private boolean status;
    private String message;
    private Data data;

    public boolean isSuccessful() {
        return status && data != null && "success".equalsIgnoreCase(data.getStatus());
    }

@Getter
@Setter
    public static class Data {
        private long id;
        private String domain;
        private String status;
        private String reference;
        private long amount;
        private String message;
        private String gateway_response;
        private LocalDateTime paidAt;
        private LocalDateTime createdAt;
        private String channel;
        private String currency;
        private LocalDateTime paid_at;
        private LocalDateTime created_at;
        private String ip_address;
        private String receipt_number;
        private String order_id;
        private String connect;
        private String metadata;
        private Log log;
        private long fees;
        private String fees_split;
        private Authorization authorization;
        private Customer customer;
        private String plan;
        private Split split;
        private String orderId;
        private long requested_amount;
        private String pos_transaction_data;
        private String source;
        private String fees_breakdown;
        private LocalDateTime transaction_date;
        private PlanObject plan_object;
        private Subaccount subaccount;

        // Getters and setters
@Getter
@Setter
        public static class Log {
            private long start_time;
            private long time_spent;
            private int attempts;
            private int errors;
            private boolean success;
            private boolean mobile;
            private Input[] input;
            private History[] history;

            // Getters and setters
@Getter
@Setter
            public static class History {
                private String type;
                private String message;
                private long time;

                // Getters and setters
            }
        }
@Getter
@Setter
        public static class Authorization {
            private String authorization_code;
            private String bin;
            private String last4;
//            @JsonProperty("exp_month")
            private String exp_month;
//            @JsonProperty("exp_year")
            private String exp_year;
            private String channel;
//            @JsonProperty("card_type")
            private String card_type;
            private String bank;
            private String country_code;
            private String brand;
            private boolean reusable;
            private String signature;
            private String account_name;

            // Getters and setters
        }
@Getter
@Setter
        public static class Customer {
            private long id;
            private String first_name;
            private String last_name;
            private String email;
            private String customer_code;
            private String phone;
            private String metadata;
            private String risk_action;
            private String international_format_phone;

            // Getters and setters
        }
@Getter
@Setter
        public static class Split {
        private String split;

        }
@Getter
@Setter
        public static class PlanObject {
    private String plan_object;

        }
@Getter
@Setter
        public static class Subaccount {
    private String subaccount;

        }
@Getter
@Setter
        public static class Input {
    private String input;

        }
    }

}

package com.paralex.erp.entities;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Date;

@Data
@RequiredArgsConstructor
@Document(collection = "otp")
public class Otp {

    @Id
    private String id;

    @Field(value = "email")
    private String email;

    @Field(value = "otp")
    private String otp;

    @Field(value = "phone_number")
    private String phoneNumber;

    @Field(value = "created_at")
    private Date createdAt = Date.from(Instant.now());

    @Field(value = "customer_id")
    private String customerId;

    // Add this for a manual approach to expire logic if needed
    public boolean isExpired() {
        return createdAt != null && createdAt.before(Date.from(Instant.now().minusSeconds(60)));
    }
}

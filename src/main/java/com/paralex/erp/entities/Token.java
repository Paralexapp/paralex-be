package com.paralex.erp.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "tokens")
public class Token {
    @Id
    private String id;

    @Field("token")
    private String token;

    @Field("isRevoked")
    private Boolean isRevoked;

    @Field("isExpired")
    private Boolean isExpired;

    @Field("customerId")
    private String customerId;
}

package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "walletTransactions")  // MongoDB-specific annotation
public class WalletTransactionEntity {

    @Id
    private String id;  // MongoDB will handle the ID generation, typically using ObjectId

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String walletId;

    @NotNull
    private int amount;

    @NotNull
    private String type;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String creatorId;

    @DBRef  // Reference to the UserEntity (stored as a separate document)
    private UserEntity creator;

    private LocalDateTime time;  // MongoDB will store LocalDateTime as a Date type
}

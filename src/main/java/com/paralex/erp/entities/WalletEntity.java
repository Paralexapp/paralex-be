package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "wallets")  // MongoDB-specific annotation
public class WalletEntity {

    @Id
    private String id;  // MongoDB uses String or ObjectId as primary key

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean status;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String creatorId;

    @DBRef  // Reference to the UserEntity (stored as a separate document)
    private UserEntity creator;

    private LocalDateTime time;

}

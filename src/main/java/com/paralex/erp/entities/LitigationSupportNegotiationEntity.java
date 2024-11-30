package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "litigationSupportNegotiations")
public class LitigationSupportNegotiationEntity {
    @Id
    private String id;

    @NotNull
    @Field(value = "amount")
    @Setter
    private int amount;

    @NotNull
    @Field(value = "litigationSupportRequestId")
    @Setter
    private String litigationSupportRequestId;

    @JsonBackReference
    @DBRef
    @Field(value = "litigationSupportRequest")
    private LitigationSupportRequestEntity litigationSupportRequest;

    @NotNull
    @Field(value = "acceptedByClient")
    @Setter
    private boolean acceptedByClient;

    @NotNull
    @Field(value = "acceptedByLawyer")
    @Setter
    private boolean acceptedByLawyer;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field(value = "creatorId")
    @Setter
    private String creatorId;

    @DBRef
    @Field(value = "creator")
    private UserEntity creator; // Assuming UserEntity is a MongoDB document

    @Field(value = "time")
    private LocalDateTime time;
}

package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bailBondAdjournmentDates")
public class BailBondAdjournmentDateEntity {

    @Id
    private String id;

    @NotNull
    @Field("adjournmentDate")
    @Setter
    private LocalDate adjournmentDate;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field("bailBondId")
    @Setter
    private String bailBondId;

    @DBRef
    private BailBondEntity bailBond;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field("creatorId")
    @Setter
    private String creatorId;

    @DBRef
    private UserEntity creator;

    @Field("time")
    private LocalDateTime time;
}

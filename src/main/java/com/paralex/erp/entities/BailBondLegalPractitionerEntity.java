package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bailBondLegalPractitioners")
public class BailBondLegalPractitionerEntity {

    @Id
    private String id;

    @Field("representativeName")
    private String representativeName;

    @Field("nameOfFirm")
    private String nameOfFirm;

    @Field("address")
    private String address;

    @Field("phoneNumber")
    private String phoneNumber;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field("bailBondId")
    private String bailBondId;

    @JsonBackReference
    @Field("bailBond")
    private BailBondEntity bailBond;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field("creatorId")
    private String creatorId;

    @Field("creator")
    private UserEntity creator;

    @Field("time")
    private LocalDateTime time;
}

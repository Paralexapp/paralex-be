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
@Document(collection = "bailBondThirdPartyGuarantors")
public class BailBondThirdPartyGuarantorEntity {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("currentAddress")
    private String currentAddress;

    @Field("currentEmployer")
    private String currentEmployer;

    @Field("currentEmployerAddress")
    private String currentEmployerAddress;

    @Field("nationalIdentityNumber")
    private String nationalIdentityNumber;

    @Field("internationalPassport")
    private String internationalPassport;

    @Field("driversLicense")
    private String driversLicense;

    @Field("taxIdentityNumber")
    private String taxIdentityNumber;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field("bailBondId")
    private String bailBondId;

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

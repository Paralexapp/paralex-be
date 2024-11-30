package com.paralex.erp.entities;

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
@Document(collection = "bailBondNextOfKinDetails")
public class BailBondNextOfKinDetailEntity {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("relationship")
    private String relationship;

    @Field("homePhone")
    private String homePhone;

    @Field("workPhone")
    private String workPhone;

    @Field("email")
    private String email;

    @Field("nationalIdentityNumber")
    private String nationalIdentityNumber;

    @Field("driversLicense")
    private String driversLicense;

    @Field("internationalPassport")
    private String internationalPassport;

    @Field("homeAddress")
    private String homeAddress;

    @Field("occupation")
    private String occupation;

    @Field("currentEmployer")
    private String currentEmployer;

    @Field("currentEmployerAddress")
    private String currentEmployerAddress;

    @Field("supervisorsName")
    private String supervisorsName;

    @Field("supervisorsPhoneNumber")
    private String supervisorsPhoneNumber;

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

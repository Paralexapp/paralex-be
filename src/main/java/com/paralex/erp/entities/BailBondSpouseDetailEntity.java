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
@Document(collection = "bailBondSpouseDetails")
public class BailBondSpouseDetailEntity {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("durationOfMarriage")
    private String durationOfMarriage;

    @Field("address")
    private String address;

    @Field("homePhone")
    private String homePhone;

    @Field("mobilePhone")
    private String mobilePhone;

    @Field("workPhone")
    private String workPhone;

    @Field("nin")
    private String nin;

    @Field("driversLicense")
    private String driversLicense;

    @Field("internationalPassport")
    private String internationalPassport;

    @Field("occupation")
    private String occupation;

    @Field("employer")
    private String employer;

    @Field("durationOfEmployment")
    private String durationOfEmployment;

    @Field("supervisorName")
    private String supervisorName;

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

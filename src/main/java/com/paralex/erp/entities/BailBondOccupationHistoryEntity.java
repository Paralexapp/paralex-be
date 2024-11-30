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
@Document(collection = "bailBondOccupationHistories")
public class BailBondOccupationHistoryEntity {

    @Id
    private String id;

    @Field("employerName")
    private String employerName;

    @Field("position")
    private String position;

    @Field("durationInYears")
    private Integer durationInYears;

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

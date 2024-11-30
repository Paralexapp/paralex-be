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
@Document(collection = "bailBondTravelOutsideJurisdictions")
public class BailBondTravelOutsideJurisdictionEntity {

    @Id
    private String id;

    @Field("lastTravelOutsideJurisdiction")
    private String lastTravelOutsideJurisdiction;

    @Field("durationOfTrip")
    private String durationOfTrip;

    @Field("destination")
    private String destination;

    @Field("nextPlannedTripOutsideJurisdiction")
    private String nextPlannedTripOutsideJurisdiction;

    @Field("destinationOfPlannedTrip")
    private String destinationOfPlannedTrip;

    @Field("nextOutOfCountryTrip")
    private String nextOutOfCountryTrip;

    @Field("nextOutOfCountryDestination")
    private String nextOutOfCountryDestination;

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

package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bailBondTravelOutsideJurisdictions")
@Entity
@DynamicUpdate
@DynamicInsert
public class BailBondTravelOutsideJurisdictionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "lastTravelOutsideJurisdiction", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String lastTravelOutsideJurisdiction;

    @Column(name = "durationOfTrip", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String durationOfTrip;

    @Column(name = "destination", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String destination;

    @Column(name = "nextPlannedTripOutsideJurisdiction", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String nextPlannedTripOutsideJurisdiction;

    @Column(name = "destinationOfPlannedTrip", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String destinationOfPlannedTrip;

    @Column(name = "nextOutOfCountryTrip", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String nextOutOfCountryTrip;

    @Column(name = "nextOutOfCountryDestination", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String nextOutOfCountryDestination;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "bailBondId", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String bailBondId;

    @OneToOne
    @JoinColumn(name = "bailBondId", insertable = false, updatable = false)
    private BailBondEntity bailBond;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "creatorId", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String creatorId;

    @OneToOne
    @JoinColumn(name = "creatorId", insertable = false, updatable = false)
    private UserEntity creator;

    @Column(name = "time", unique = false, nullable = true, columnDefinition = "TIMESTAMP NOT NULL DEFAULT NOW()", insertable = true, updatable = false)
    private LocalDateTime time;
}

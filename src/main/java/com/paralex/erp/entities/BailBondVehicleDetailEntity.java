package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "bailBondVehicleDetails")
@Entity
@DynamicUpdate
@DynamicInsert
public class BailBondVehicleDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "year", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String year;

    @Column(name = "make", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String make;

    @Column(name = "model", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String model;

    @Column(name = "color", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String color;

    @Column(name = "plateNumber", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String plateNumber;

    @Column(name = "state", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String state;

    @Column(name = "insuranceCompanyOrAgent", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String insuranceCompanyOrAgent;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "bailBondId", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String bailBondId;

    @JsonBackReference
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

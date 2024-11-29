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
@Table(name = "driverProfiles")
@Entity
@DynamicUpdate
@DynamicInsert
public class DriverProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @Column(name = "hasRiderCard", columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean hasRiderCard;

    @NotNull
    @Column(name = "hasBike", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean hasBike;

    @NotNull
    @Column(name = "bikeType", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String bikeType;

    @NotNull
    @Column(name = "bikeCapacity", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String bikeCapacity;

    @NotNull
    @Column(name = "chassisNumber", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String chassisNumber;

    @NotNull
    @Column(name = "guarantorClass", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String guarantorClass;

    @NotNull
    @Column(name = "guarantorPhoneNumber", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String guarantorPhoneNumber;

    @NotNull
    @Column(name = "guarantorEmail", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String guarantorEmail;

    @NotNull
    @Column(name = "guarantorStateOfResidence", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String guarantorStateOfResidence;

    @NotNull
    @Column(name = "guarantorResidentialAddress", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String guarantorResidentialAddress;

    @NotNull
    @Column(name = "bvn", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String bvn;

    @NotNull
    @Column(name = "nin", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String nin;

    @NotNull
    @Column(name = "bankCode", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String bankCode;

    @NotNull
    @Column(name = "bankName", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String bankName;

    @NotNull
    @Column(name = "accountNumber", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String accountNumber;

    @NotNull
    @Column(name = "accountName", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String accountName;

    @Column(name = "passportUrl", unique = false, nullable = true, insertable = true, updatable = true)
    @Setter
    private String passportUrl;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "offline", columnDefinition = "BOOLEAN NOT NULL DEFAULT TRUE", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean offline;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "userId", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String userId;

    @OneToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private UserEntity user;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "creatorId", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String creatorId;

    @OneToOne
    @JoinColumn(name = "creatorId", insertable = false, updatable = false)
    private UserEntity creator;

    @NotNull
    @Column(name = "status", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean status;

    @Column(name = "time", unique = false, nullable = true, columnDefinition = "TIMESTAMP NOT NULL DEFAULT NOW()", insertable = true, updatable = false)
    private LocalDateTime time;
}

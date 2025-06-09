package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "driverProfiles")
public class DriverProfileEntity {
    @Id
    private String id;

    @Field(value = "hasRiderCard", write = Field.Write.NON_NULL)
    @Setter
    private boolean hasRiderCard;

    @Field(value = "hasBike", write = Field.Write.NON_NULL)
    @Setter
    private boolean hasBike;

    @Field(value = "bikeType", write = Field.Write.NON_NULL)
    @Setter
    private String bikeType;

    @Field(value = "bikeCapacity", write = Field.Write.NON_NULL)
    @Setter
    private String bikeCapacity;

    @Field(value = "chassisNumber", write = Field.Write.NON_NULL)
    @Setter
    private String chassisNumber;

    @Field(value = "guarantorClass", write = Field.Write.NON_NULL)
    @Setter
    private String guarantorClass;

    @Field(value = "guarantorPhoneNumber", write = Field.Write.NON_NULL)
    @Setter
    private String guarantorPhoneNumber;

    @Field(value = "guarantorEmail", write = Field.Write.NON_NULL)
    @Setter
    private String guarantorEmail;

    @Field(value = "guarantorStateOfResidence", write = Field.Write.NON_NULL)
    @Setter
    private String guarantorStateOfResidence;

    @Field(value = "guarantorResidentialAddress", write = Field.Write.NON_NULL)
    @Setter
    private String guarantorResidentialAddress;

    @Field(value = "bvn", write = Field.Write.NON_NULL)
    @Setter
    private String bvn;

    @Field(value = "nin", write = Field.Write.NON_NULL)
    @Setter
    private String nin;

    @Field(value = "bankCode", write = Field.Write.NON_NULL)
    @Setter
    private String bankCode;

    @Field(value = "bankName", write = Field.Write.NON_NULL)
    @Setter
    private String bankName;

    @Field(value = "accountNumber", write = Field.Write.NON_NULL)
    @Setter
    private String accountNumber;

    @Field(value = "accountName", write = Field.Write.NON_NULL)
    @Setter
    private String accountName;

    @Field(value = "passportUrl", write = Field.Write.NON_NULL)
    @Setter
    private String passportUrl;

    @Field(value = "offline", write = Field.Write.NON_NULL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Setter
    private boolean offline;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    @Field("location")
    @Schema(description = "Location of the driver", example = "[0, 0]")
    private GeoJsonPoint location;


    @Field(value = "userId", write = Field.Write.NON_NULL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Setter
    private String userId;

//    @Field(value = "user", write = Field.Write.NON_NULL)
//    @Setter
    @DBRef
    private UserEntity user;

    @Field("averageRating")
    private double averageRating;

    @Field("totalReviews")
    private int totalReviews;

    @Field(value = "creatorId", write = Field.Write.NON_NULL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Setter
    private String creatorId;

    @Field(value = "creator", write = Field.Write.NON_NULL)
    @Setter
    private UserEntity creator; // Assuming UserEntity is mapped to a MongoDB document

    @Field(value = "status", write = Field.Write.NON_NULL)
    @Setter
    private boolean status;

    @Field(value = "time", write = Field.Write.NON_NULL)
    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();
}

package com.paralex.erp.entities;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "countries")
public class CountryEntity {

    @Id
    private String id;  // Mongo uses String for id instead of int

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("latitude")
    private double latitude;

    @NotNull
    @Field("longitude")
    private double longitude;

    @NotNull
    @Field("iso3")
    private String iso3;

    @NotNull
    @Field("iso2")
    private String iso2;

    @NotNull
    @Field("emoji")
    private String emoji;

    @NotNull
    @Field("phoneCode")
    private String phoneCode;
}

package com.paralex.erp.entities;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "states")
public class StateEntity {

    @Id
    private String id;

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
    @Field("country_id")
    private String countryId;

    @NotNull
    @Field("country_code")
    private String countryCode;

    @NotNull
    @Field("iso2")
    private String iso2;
}

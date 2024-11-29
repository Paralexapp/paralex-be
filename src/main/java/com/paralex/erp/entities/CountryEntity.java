package com.paralex.erp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "countries")
@Entity
@DynamicUpdate
@DynamicInsert
public class CountryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @NotNull
    @Column(name = "name", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String name;

    @NotNull
    @Column(name = "latitude", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private double latitude;

    @NotNull
    @Column(name = "longitude", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private double longitude;

    @NotNull
    @Column(name = "iso3", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String iso3;

    @NotNull
    @Column(name = "iso2", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String iso2;

    @NotNull
    @Column(name = "emoji", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String emoji;

    @NotNull
    @Column(name = "phonecode", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String phoneCode;
}

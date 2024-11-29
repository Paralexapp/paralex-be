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
@Table(name = "bailBondThirdPartyGuarantors")
@Entity
@DynamicUpdate
@DynamicInsert
public class BailBondThirdPartyGuarantorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String name;

    @Column(name = "currentAddress", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String currentAddress;

    @Column(name = "currentEmployer", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String currentEmployer;

    @Column(name = "currentEmployerAddress", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String currentEmployerAddress;

    @Column(name = "nationalIdentityNumber", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String nationalIdentityNumber;

    @Column(name = "internationalPassport", unique = false, nullable = true, insertable = true, updatable = false)
    @Setter
    private String internationalPassport;

    @Column(name = "driversLicense", unique = false, nullable = true, insertable = true, updatable = false)
    @Setter
    private String driversLicense;

    @Column(name = "taxIdentityNumber", unique = false, nullable = true, insertable = true, updatable = false)
    @Setter
    private String taxIdentityNumber;

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

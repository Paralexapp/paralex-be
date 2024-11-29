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
@Table(name = "bailBondNextOfKinDetails")
@Entity
@DynamicUpdate
@DynamicInsert
public class BailBondNextOfKinDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String name;

    @Column(name = "relationship", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String relationship;

    @Column(name = "homePhone", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String homePhone;

    @Column(name = "workPhone", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String workPhone;

    @Column(name = "email", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String email;

    @Column(name = "nationalIdentityNumber", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String nationalIdentityNumber;

    @Column(name = "driversLicense", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String driversLicense;

    @Column(name = "internationalPassport", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String internationalPassport;

    @Column(name = "homeAddress", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String homeAddress;

    @Column(name = "occupation", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String occupation;

    @Column(name = "currentEmployer", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String currentEmployer;

    @Column(name = "currentEmployerAddress", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String currentEmployerAddress;

    @Column(name = "supervisorsName", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String supervisorsName;

    @Column(name = "supervisorsPhoneNumber", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String supervisorsPhoneNumber;

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

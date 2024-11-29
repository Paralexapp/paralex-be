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
@Table(name = "bailBondSpouseDetails")
@Entity
@DynamicUpdate
@DynamicInsert
public class BailBondSpouseDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String name;

    @Column(name = "durationOfMarriage", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String durationOfMarriage;

    @Column(name = "address", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String address;

    @Column(name = "homePhone", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String homePhone;

    @Column(name = "mobilePhone", unique = false, nullable = true, insertable = true, updatable = false)
    @Setter
    private String mobilePhone;

    @Column(name = "workPhone", unique = false, nullable = true, insertable = true, updatable = false)
    @Setter
    private String workPhone;

    @Column(name = "nin", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String nin;

    @Column(name = "driversLicense", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String driversLicense;

    @Column(name = "internationalPassport", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String internationalPassport;

    @Column(name = "occupation", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String occupation;

    @Column(name = "employer", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String employer;

    @Column(name = "durationOfEmployment", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String durationOfEmployment;

    @Column(name = "supervisorName", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String supervisorName;

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

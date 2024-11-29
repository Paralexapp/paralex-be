package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bailBondLandDetails")
@Entity
@DynamicUpdate
@DynamicInsert
public class BailBondLandDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "address", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String address;

    @Column(name = "state", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String state;

    @Column(name = "dateOfPurchase", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private LocalDate dateOfPurchase;

    @Column(name = "certificateNumber", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String certificateNumber;

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

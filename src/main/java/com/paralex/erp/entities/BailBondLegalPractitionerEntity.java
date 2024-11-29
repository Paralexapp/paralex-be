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
@Table(name = "bailBondLegalPractitioners")
@Entity
@DynamicUpdate
@DynamicInsert
public class BailBondLegalPractitionerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "representativeName", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String representativeName;

    @Column(name = "nameOfFirm", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String nameOfFirm;

    @Column(name = "address", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String address;

    @Column(name = "phoneNumber", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String phoneNumber;

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

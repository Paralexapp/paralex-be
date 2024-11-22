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
@Table(name = "transactionRequirementOptions")
@Entity
@DynamicUpdate
@DynamicInsert
public class TransactionRequirementOptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "label", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String label;

    @Column(name = "data", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String data;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "transactionRequirementId", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String transactionRequirementId;

    @OneToOne
    @JoinColumn(name = "transactionRequirementId", insertable = false, updatable = false)
    private TransactionRequirementEntity transactionRequirement;

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

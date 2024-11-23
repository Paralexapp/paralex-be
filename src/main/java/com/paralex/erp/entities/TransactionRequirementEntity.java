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
@Table(name = "transactionRequirements")
@Entity
@DynamicUpdate
@DynamicInsert
public class TransactionRequirementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "label", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String label;

    @Column(name = "errorMessage", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String errorMessage;

    @Column(name = "description", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String description;

    @Column(name = "index", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private int index;

    @Column(name = "step", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private int step;

    @Column(name = "type", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String type;

    @NotNull
    @Column(name = "required", columnDefinition = "BOOLEAN NOT NULL DEFAULT TRUE", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean required;

    @NotNull
    @Column(name = "hasOption", columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean hasOption;

    @NotNull
    @Column(name = "multiple", columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean multiple;

    @Column(name = "max", columnDefinition = "INTEGER NOT NULL DEFAULT 0", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private int max;

    @Column(name = "min", columnDefinition = "INTEGER NOT NULL DEFAULT 1", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private int min;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "transactionId", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String transactionId;

    @OneToOne
    @JoinColumn(name = "transactionId", insertable = false, updatable = false)
    private TransactionEntity transaction;

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

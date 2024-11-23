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
@Table(name = "deliveryStages")
@Entity
@DynamicUpdate
@DynamicInsert
public class DeliveryStageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @Column(name = "name", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String name;

    @NotNull
    @Column(name = "initial", columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean initial;

    @NotNull
    @Column(name = "terminal", columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean terminal;

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

    @NotNull
    @Column(name = "forDriver", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean forDriver;

    @NotNull
    @Column(name = "forAdmin", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean forAdmin;

    @NotNull
    @Column(name = "shouldNotify", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean shouldNotify;

    @Column(name = "time", unique = false, nullable = true, columnDefinition = "TIMESTAMP NOT NULL DEFAULT NOW()", insertable = true, updatable = false)
    private LocalDateTime time;
}

package com.paralex.erp.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "locations")
@Entity
@DynamicUpdate
@DynamicInsert
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @Column(name = "name", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private String name;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "status", columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private boolean status;

    @NotNull
    @Column(name = "location", columnDefinition = "GEOGRAPHY", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private Point location;

    // INFO so, we use this for delivery location pricing, and we use states and cities for finding lawyers...
    // INFO price per kilometer
    @NotNull
    @Column(name = "amount", unique = false, nullable = false, insertable = true, updatable = true)
    @Setter
    private int amount;

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

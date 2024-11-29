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
@Table(name = "walletTransactions")
@Entity
@DynamicUpdate
@DynamicInsert
public class WalletTransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @Column(name = "walletId", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String walletId;

    @NotNull
    @Column(name = "amount", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private int amount;

    @NotNull
    @Column(name = "type", unique = false, nullable = false, insertable = true, updatable = false)
    @Setter
    private String type;

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

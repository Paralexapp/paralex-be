package com.paralex.erp.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Date;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "otp")
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String otp;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt = Date.from(Instant.now());

    @Column(name = "customer_id")
    private String customerId;

    // Add this for a manual approach to expire logic if needed
    @Transient
    private boolean isExpired() {
        return createdAt != null && createdAt.before(Date.from(Instant.now().minusSeconds(60)));
    }
}

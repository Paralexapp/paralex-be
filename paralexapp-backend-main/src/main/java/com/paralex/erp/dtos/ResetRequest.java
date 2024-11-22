package com.paralex.erp.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reset_request")
@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResetRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reset_token", nullable = false)
    private String resetToken;

    @Column(name = "reset_token_expiry", nullable = false)
    private LocalDateTime resetTokenExpiry;

    @Column(name = "customer_id", nullable = false)
    private String customerId;
}

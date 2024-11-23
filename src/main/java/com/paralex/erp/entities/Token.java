package com.paralex.erp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tokens")
@Data
public class Token {
    @Id
    private String id;
    private String token;
    private Boolean isRevoked;
    private Boolean isExpired;
    private String customerId;
}

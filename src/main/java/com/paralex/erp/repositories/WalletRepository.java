package com.paralex.erp.repositories;

import com.paralex.erp.entities.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WalletRepository extends MongoRepository<WalletEntity, String> {
}

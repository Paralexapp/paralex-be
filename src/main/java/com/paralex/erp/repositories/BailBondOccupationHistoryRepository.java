package com.paralex.erp.repositories;

import com.paralex.erp.entities.BailBondOccupationHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BailBondOccupationHistoryRepository extends MongoRepository<BailBondOccupationHistoryEntity, String> {
}

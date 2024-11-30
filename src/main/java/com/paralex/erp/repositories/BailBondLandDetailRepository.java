package com.paralex.erp.repositories;

import com.paralex.erp.entities.BailBondLandDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BailBondLandDetailRepository extends MongoRepository<BailBondLandDetailEntity, String> {
}

package com.paralex.erp.repositories;

import com.paralex.erp.entities.BailBondVehicleDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BailBondVehicleDetailRepository extends MongoRepository<BailBondVehicleDetailEntity, String> {
}

package com.paralex.erp.repositories;

import com.paralex.erp.entities.BailBondTravelOutsideJurisdictionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BailBondTravelOutsideJurisdictionRepository extends MongoRepository<BailBondTravelOutsideJurisdictionEntity, String> {
}

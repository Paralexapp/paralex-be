package com.paralex.erp.repositories;

import com.paralex.erp.entities.BailBondNextOfKinDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BailBondNextOfKinDetailRepository extends MongoRepository<BailBondNextOfKinDetailEntity, String> {
}

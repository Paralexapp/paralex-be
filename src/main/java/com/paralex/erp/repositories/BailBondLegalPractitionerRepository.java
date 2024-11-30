package com.paralex.erp.repositories;

import com.paralex.erp.entities.BailBondLegalPractitionerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BailBondLegalPractitionerRepository extends MongoRepository<BailBondLegalPractitionerEntity, String> {
}

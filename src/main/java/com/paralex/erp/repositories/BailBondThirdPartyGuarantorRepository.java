package com.paralex.erp.repositories;

import com.paralex.erp.entities.BailBondThirdPartyGuarantorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BailBondThirdPartyGuarantorRepository extends MongoRepository<BailBondThirdPartyGuarantorEntity, String> {
}

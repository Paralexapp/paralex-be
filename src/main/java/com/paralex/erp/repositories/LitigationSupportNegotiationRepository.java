package com.paralex.erp.repositories;

import com.paralex.erp.entities.LitigationSupportNegotiationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LitigationSupportNegotiationRepository extends MongoRepository<LitigationSupportNegotiationEntity, String> {
}

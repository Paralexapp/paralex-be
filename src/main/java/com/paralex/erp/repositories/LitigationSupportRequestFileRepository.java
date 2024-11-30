package com.paralex.erp.repositories;

import com.paralex.erp.entities.LitigationSupportRequestFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LitigationSupportRequestFileRepository extends MongoRepository<LitigationSupportRequestFileEntity, String> {
}

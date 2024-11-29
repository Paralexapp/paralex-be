package com.paralex.erp.repositories;

import com.paralex.erp.entities.DeliveryStageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeliveryStageRepository extends MongoRepository<DeliveryStageDocument, String> {
}

package com.paralex.erp.repositories;

import com.paralex.erp.documents.DeliveryRequestDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeliveryRequestRepository extends MongoRepository<DeliveryRequestDocument, String> {
}

package com.paralex.erp.repositories;

import com.paralex.erp.documents.DeliveryPackageCategoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeliveryPackageCategoryRepository extends MongoRepository<DeliveryPackageCategoryDocument, String> {
}

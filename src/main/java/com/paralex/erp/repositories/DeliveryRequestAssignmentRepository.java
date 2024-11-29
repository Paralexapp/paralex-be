package com.paralex.erp.repositories;

import com.paralex.erp.documents.DeliveryRequestAssignmentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeliveryRequestAssignmentRepository extends MongoRepository<DeliveryRequestAssignmentDocument, String> {
}

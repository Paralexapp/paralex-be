package com.paralex.erp.repositories;

import com.paralex.erp.documents.DeliveryRequestAssignmentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DeliveryRequestAssignmentRepository extends MongoRepository<DeliveryRequestAssignmentDocument, String> {
    Optional<DeliveryRequestAssignmentDocument> findByDeliveryRequestId(String deliveryRequestId);
}

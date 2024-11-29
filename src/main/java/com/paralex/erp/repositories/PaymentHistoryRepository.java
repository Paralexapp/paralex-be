package com.paralex.erp.repositories;

import com.paralex.erp.documents.PaymentHistoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentHistoryRepository extends MongoRepository<PaymentHistoryDocument, String> {
}

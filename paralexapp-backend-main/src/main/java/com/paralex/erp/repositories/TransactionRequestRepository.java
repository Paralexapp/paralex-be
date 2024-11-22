package com.paralex.erp.repositories;

import com.paralex.erp.documents.TransactionRequestDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRequestRepository extends MongoRepository<TransactionRequestDocument, String> {
}

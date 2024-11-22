package com.paralex.erp.repositories;

import com.paralex.erp.documents.TransactionRequestSubmissionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRequestSubmissionRepository extends MongoRepository<TransactionRequestSubmissionDocument, String> {
}

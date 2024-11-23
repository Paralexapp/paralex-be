package com.paralex.erp.repositories;

import com.paralex.erp.documents.AuthorizationRecordDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorizationRepository extends MongoRepository<AuthorizationRecordDocument, String> {
}

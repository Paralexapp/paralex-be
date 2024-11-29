package com.paralex.erp.repositories;

import com.paralex.erp.documents.DriverPositionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DriverPositionRepository extends MongoRepository<DriverPositionDocument, String> {

}

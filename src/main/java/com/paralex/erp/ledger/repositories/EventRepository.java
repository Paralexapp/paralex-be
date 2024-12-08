package com.paralex.erp.ledger.repositories;

import com.paralex.erp.ledger.documents.Events;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends MongoRepository<Events,String> {
}

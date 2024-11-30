package com.paralex.erp.repositories;

import com.paralex.erp.entities.CourtEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourtRepository extends MongoRepository<CourtEntity, String> {
}

package com.paralex.erp.repositories;

import com.paralex.erp.entities.LitigationSupportRequestEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LitigationSupportRequestRepository extends MongoRepository<LitigationSupportRequestEntity, String>, CustomLitigationSupportRequestRepository {

}

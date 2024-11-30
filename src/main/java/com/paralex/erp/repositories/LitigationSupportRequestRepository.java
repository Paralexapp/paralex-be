package com.paralex.erp.repositories;

import com.paralex.erp.entities.LitigationSupportRequestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LitigationSupportRequestRepository extends MongoRepository<LitigationSupportRequestEntity, String>, CustomLitigationSupportRequestRepository {

    @Autowired
    MongoTemplate mongoTemplate = null;

    // Method to update the 'paid' field for a specific paymentRequestCode
    default void paidForBailBond(String paymentRequestCode, boolean paid) {
        Query query = new Query(Criteria.where("paymentRequestCode").is(paymentRequestCode));  // Find by paymentRequestCode
        Update update = new Update().set("paid", paid);  // Update the 'paid' field
        mongoTemplate.updateFirst(query, update, LitigationSupportRequestEntity.class);  // Perform the update
    }

     default Page<LitigationSupportRequestEntity> findAll(Criteria criteria, PageRequest pageable) {
        Query query = new Query(criteria).with(pageable);

        // Get total count
        long totalCount = mongoTemplate.count(query, LitigationSupportRequestEntity.class);

        // Fetch results
        List<LitigationSupportRequestEntity> results = mongoTemplate.find(query, LitigationSupportRequestEntity.class);

        // Wrap results in a Page object
        return new PageImpl<>(results, pageable, totalCount);
    }
}

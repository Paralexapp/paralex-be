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
public interface LitigationSupportRequestRepository extends MongoRepository<LitigationSupportRequestEntity, String> {

    @Autowired
    MongoTemplate mongoTemplate = null;

    // Method to update the 'paid' field for a specific paymentRequestCode
    default void paidForBailBond(String paymentRequestCode, boolean paid) {
        Query query = new Query(Criteria.where("paymentRequestCode").is(paymentRequestCode));  // Find by paymentRequestCode
        Update update = new Update().set("paid", paid);  // Update the 'paid' field
        mongoTemplate.updateFirst(query, update, LitigationSupportRequestEntity.class);  // Perform the update
    }

    // MongoDB equivalent of 'findAll' with dynamic queries and pagination
    default Page<LitigationSupportRequestEntity> findAll(Criteria criteria, PageRequest pageable) {
        // Create a query object with the provided criteria
        Query query = new Query(criteria);

        // Apply pagination
        query.with(pageable);

        // Get total count for pagination purposes
        long totalCount = mongoTemplate.count(query, LitigationSupportRequestEntity.class);

        // Apply pagination (limit & skip)
        query.limit(pageable.getPageSize());  // Set page size (limit)
        query.skip(pageable.getOffset());  // Set page offset (skip)

        // Execute the query and retrieve the results
        List<LitigationSupportRequestEntity> results = mongoTemplate.find(query, LitigationSupportRequestEntity.class);

        // Return the results wrapped in a Page object
        return new PageImpl<>(results, pageable, totalCount);
    }
}

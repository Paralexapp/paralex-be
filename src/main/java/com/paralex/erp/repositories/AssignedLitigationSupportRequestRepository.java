package com.paralex.erp.repositories;

import com.google.api.client.http.LowLevelHttpResponse;
import com.paralex.erp.entities.AssignedLitigationSupportRequestEntity;
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
public interface AssignedLitigationSupportRequestRepository extends MongoRepository<AssignedLitigationSupportRequestEntity, String> {

    @Autowired
    MongoTemplate mongoTemplate = null;

    // Method to update "accepted" and "rejected" fields to true/false respectively
    default void accept(String id) {
        // Use MongoTemplate for custom update
        mongoTemplate.updateFirst(
                new Query().addCriteria(Criteria.where("id").is(id)),
                new Update().set("accepted", true).set("rejected", false),
                AssignedLitigationSupportRequestEntity.class
        );
    }

    // Method to update "accepted" and "rejected" fields to false/true respectively
    default void reject(String id) {
        // Use MongoTemplate for custom update
        mongoTemplate.updateFirst(
                new Query().addCriteria(Criteria.where("id").is(id)),
                new Update().set("accepted", false).set("rejected", true),
                AssignedLitigationSupportRequestEntity.class
        );
    }

    // MongoDB equivalent of 'findAll' with dynamic queries and pagination
    default Page<AssignedLitigationSupportRequestEntity> findAll(Criteria criteria, PageRequest pageable) {
        // Create a query object with the provided criteria
        Query query = new Query(criteria);

        // Apply pagination
        query.with(pageable);

        // Get total count for pagination purposes
        long totalCount = mongoTemplate.count(query, AssignedLitigationSupportRequestEntity.class);

        // Apply pagination (limit & skip)
        query.limit(pageable.getPageSize());  // Set page size (limit)
        query.skip(pageable.getOffset());  // Set page offset (skip)

        // Execute the query and retrieve the results
        List<AssignedLitigationSupportRequestEntity> results = mongoTemplate.find(query, AssignedLitigationSupportRequestEntity.class);

        // Return the results wrapped in a Page object
        return new PageImpl<>(results, pageable, totalCount);
    }
}

package com.paralex.erp.repositories;

import com.paralex.erp.entities.DriverProfileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverProfileRepository extends MongoRepository<DriverProfileEntity, String> {

    // Custom query methods
    Optional<DriverProfileEntity> findByUserId(String userId);

    List<DriverProfileEntity> findByIdIn(List<String> driverProfileIds);

    // No need to implement basic save/find methods as MongoRepository already provides them
}



//package com.paralex.erp.repositories;
//
//import com.paralex.erp.entities.DriverProfileEntity;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.*;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.repository.query.FluentQuery;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.function.Function;
//
//@Repository
//public class DriverProfileRepository implements MongoRepository<DriverProfileEntity, String> {
//
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    // Enable Profile by UserId
//    public void enableProfileByUserId(String userId) {
//        mongoTemplate.updateFirst(
//                new Query().addCriteria(Criteria.where("userId").is(userId)),
//                new Update().set("status", false),
//                DriverProfileEntity.class
//        );
//    }
//
//    // Disable Profile by UserId
//    public void disableProfileByUserId(String userId) {
//        mongoTemplate.updateFirst(
//                new Query().addCriteria(Criteria.where("userId").is(userId)),
//                new Update().set("status", true),
//                DriverProfileEntity.class
//        );
//    }
//
//    // Find by List of Driver Profile IDs
//    public List<DriverProfileEntity> findByIdIn(List<String> driverProfileIds) {
//        return mongoTemplate.find(new Query(Criteria.where("id").in(driverProfileIds)), DriverProfileEntity.class);
//    }
//
//    // Find by UserId
//    public Optional<DriverProfileEntity> findByUserId(String userId) {
//        return Optional.ofNullable(mongoTemplate.findOne(new Query(Criteria.where("userId").is(userId)), DriverProfileEntity.class));
//    }
//
//    // MongoDB equivalent of 'findAll' with pagination and dynamic queries
//    public Page<DriverProfileEntity> findAll(Criteria criteria, PageRequest pageable) {
//        // Create a query object with the provided criteria
//        Query query = new Query(criteria);
//
//        // Apply pagination
//        query.with(pageable);
//
//        // Get total count for pagination purposes
//        long totalCount = mongoTemplate.count(query, DriverProfileEntity.class);
//
//        // Apply pagination (limit & skip)
//        query.limit(pageable.getPageSize());  // Set page size (limit)
//        query.skip(pageable.getOffset());  // Set page offset (skip)
//
//        // Execute the query and retrieve the results
//        List<DriverProfileEntity> results = mongoTemplate.find(query, DriverProfileEntity.class);
//
//        // Return the results wrapped in a Page object
//        return new PageImpl<>(results, pageable, totalCount);
//    }
//
//
//}

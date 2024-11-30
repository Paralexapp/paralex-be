package com.paralex.erp.repositories;

import com.paralex.erp.entities.DriverProfileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public class DriverProfileRepository implements MongoRepository<DriverProfileEntity, String> {

    @Autowired
    private MongoTemplate mongoTemplate;

    // Enable Profile by UserId
    public void enableProfileByUserId(String userId) {
        mongoTemplate.updateFirst(
                new Query().addCriteria(Criteria.where("userId").is(userId)),
                new Update().set("status", false),
                DriverProfileEntity.class
        );
    }

    // Disable Profile by UserId
    public void disableProfileByUserId(String userId) {
        mongoTemplate.updateFirst(
                new Query().addCriteria(Criteria.where("userId").is(userId)),
                new Update().set("status", true),
                DriverProfileEntity.class
        );
    }

    // Find by List of Driver Profile IDs
    public List<DriverProfileEntity> findByIdIn(List<String> driverProfileIds) {
        return mongoTemplate.find(new Query(Criteria.where("id").in(driverProfileIds)), DriverProfileEntity.class);
    }

    // Find by UserId
    public Optional<DriverProfileEntity> findByUserId(String userId) {
        return Optional.ofNullable(mongoTemplate.findOne(new Query(Criteria.where("userId").is(userId)), DriverProfileEntity.class));
    }

    // MongoDB equivalent of 'findAll' with pagination and dynamic queries
    public Page<DriverProfileEntity> findAll(Criteria criteria, PageRequest pageable) {
        // Create a query object with the provided criteria
        Query query = new Query(criteria);

        // Apply pagination
        query.with(pageable);

        // Get total count for pagination purposes
        long totalCount = mongoTemplate.count(query, DriverProfileEntity.class);

        // Apply pagination (limit & skip)
        query.limit(pageable.getPageSize());  // Set page size (limit)
        query.skip(pageable.getOffset());  // Set page offset (skip)

        // Execute the query and retrieve the results
        List<DriverProfileEntity> results = mongoTemplate.find(query, DriverProfileEntity.class);

        // Return the results wrapped in a Page object
        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    public <S extends DriverProfileEntity> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends DriverProfileEntity> List<S> insert(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public <S extends DriverProfileEntity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends DriverProfileEntity> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends DriverProfileEntity> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends DriverProfileEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends DriverProfileEntity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends DriverProfileEntity> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends DriverProfileEntity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends DriverProfileEntity> S save(S entity) {
        return null;
    }

    @Override
    public <S extends DriverProfileEntity> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<DriverProfileEntity> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<DriverProfileEntity> findAll() {
        return List.of();
    }

    @Override
    public List<DriverProfileEntity> findAllById(Iterable<String> strings) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(DriverProfileEntity entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends DriverProfileEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<DriverProfileEntity> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<DriverProfileEntity> findAll(Pageable pageable) {
        return null;
    }
}

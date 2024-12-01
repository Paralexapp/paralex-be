package com.paralex.erp.repositories;

import com.paralex.erp.entities.LawyerProfileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LawyerProfileRepository extends MongoRepository<LawyerProfileEntity, String>, CustomLawyerProfileRepository {
    Optional<LawyerProfileEntity> findByUserId(String userId);
}

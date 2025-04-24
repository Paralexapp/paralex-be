package com.paralex.erp.repositories;

import com.paralex.erp.entities.LawyerReviewEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LawyerReviewRepository extends MongoRepository<LawyerReviewEntity, String> {
    List<LawyerReviewEntity> findByLawyerProfileId(String lawyerProfileId);
}

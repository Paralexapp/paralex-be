package com.paralex.erp.repositories;

import com.paralex.erp.entities.DriverReviewEntity;
import com.paralex.erp.entities.LawyerReviewEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DriverReviewRepository extends MongoRepository<DriverReviewEntity, String> {
    List<DriverReviewEntity> findByDriverProfileId(String driverProfileId);
}

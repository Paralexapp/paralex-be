package com.paralex.erp.repositories;

import com.paralex.erp.entities.AdminNotification;
import com.paralex.erp.entities.LawyerNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminNotificationRepository extends MongoRepository<AdminNotification, String> {
    List<AdminNotification> findByUserId(String userId);
}

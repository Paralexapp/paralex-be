package com.paralex.erp.repositories;

import com.paralex.erp.entities.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String> {

    // Find a user by email
    Optional<UserEntity> findByEmail(String email);

    // Find a user by phone number
    Optional<UserEntity> findByPhoneNumber(String phoneNumber);
}

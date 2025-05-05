package org.narcissus.narcissuscoreservice.repository;

import org.narcissus.narcissuscoreservice.model.user.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
    boolean existsByEmail(String email);
}

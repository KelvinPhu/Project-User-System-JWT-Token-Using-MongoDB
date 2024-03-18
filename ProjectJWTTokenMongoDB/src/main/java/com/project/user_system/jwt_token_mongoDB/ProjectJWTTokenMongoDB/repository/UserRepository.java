package com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB.model.User;


@Repository
public interface UserRepository extends MongoRepository<User, String>{
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
	Optional<User> findByUsername(String username);
}

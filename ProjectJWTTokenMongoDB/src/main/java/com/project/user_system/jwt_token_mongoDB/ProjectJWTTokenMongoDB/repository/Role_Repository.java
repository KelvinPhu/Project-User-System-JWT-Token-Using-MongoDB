package com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB.model.EnumRole;
import com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB.model.Role;


@Repository
public interface Role_Repository extends MongoRepository<Role, String>{
	Optional<Role> findByName(EnumRole name);
}

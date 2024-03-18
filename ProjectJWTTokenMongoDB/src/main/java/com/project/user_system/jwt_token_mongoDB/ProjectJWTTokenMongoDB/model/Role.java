package com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB.model;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
	
	@Id
	private String id;
	
	private EnumRole name;
}

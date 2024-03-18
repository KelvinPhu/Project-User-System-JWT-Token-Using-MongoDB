package com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ProjectJwtTokenMongoDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectJwtTokenMongoDbApplication.class, args);
	}

}

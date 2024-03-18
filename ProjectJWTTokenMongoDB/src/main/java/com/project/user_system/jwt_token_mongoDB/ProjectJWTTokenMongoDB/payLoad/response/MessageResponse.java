package com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB.payLoad.response;

import lombok.Data;

@Data
public class MessageResponse {
	
	private String message;

	public MessageResponse(String message) {
		super();
		this.message = message;
	}
}
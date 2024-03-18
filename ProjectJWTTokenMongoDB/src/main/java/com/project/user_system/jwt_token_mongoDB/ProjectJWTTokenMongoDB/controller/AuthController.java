package com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB.model.EnumRole;
import com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB.model.Role;
import com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB.model.User;
import com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB.payLoad.dto.SigninDto;
import com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB.payLoad.dto.SignupDto;
import com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB.payLoad.response.JwtResponse;
import com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB.payLoad.response.MessageResponse;
import com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB.repository.Role_Repository;
import com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB.repository.UserRepository;
import com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB.security.jwt.JwtUtils;
import com.project.user_system.jwt_token_mongoDB.ProjectJWTTokenMongoDB.security.service.UserDetailsImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepo;

	@Autowired
	Role_Repository roleRepo;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody SigninDto signinDto) {

		Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(signinDto.getUsername(), signinDto.getPassword()));

	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    String jwt = jwtUtils.generateJwtToken(authentication);
	    
	    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
	    List<String> roles = userDetails.getAuthorities().stream()
	        .map(item -> item.getAuthority())
	        .collect(Collectors.toList());

	    return ResponseEntity.ok(new JwtResponse(jwt, 
	                         userDetails.getId(), 
	                         userDetails.getUsername(), 
	                         userDetails.getEmail(), 
	                         roles));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupDto signupDto) {
		if (userRepo.existsByUsername(signupDto.getUsername())) {
			return ResponseEntity
				.badRequest()
				.body(new MessageResponse("Error: Username is already taken!"));
	    }

	    if (userRepo.existsByEmail(signupDto.getEmail())) {
	    	return ResponseEntity
		        .badRequest()
		        .body(new MessageResponse("Error: Email is already in use!"));
	    }

	    // Create new user's account
	    User user = new User(
	    		signupDto.getUsername(), 
	    		signupDto.getEmail(),
	            encoder.encode(signupDto.getPassword()));

	    Set<String> strRoles = signupDto.getRole();
	    Set<Role> roles = new HashSet<>();

	    if (strRoles == null) {
	    	Role userRole = roleRepo.findByName(EnumRole.ROLE_USER)
	    			.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	    	roles.add(userRole);
	    }else{
	    	strRoles.forEach(role -> {
		        switch (role) {
			    	case "admin":
			    		Role adminRole = roleRepo.findByName(EnumRole.ROLE_ADMIN)
			    			.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			    		roles.add(adminRole);
		
			    		break;
			    		
			        case "mod":
			        	Role modRole = roleRepo.findByName(EnumRole.ROLE_MODERATOR)
			              	.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			        	roles.add(modRole);
		
			        	break;
			        	
			        default:
			        	Role userRole = roleRepo.findByName(EnumRole.ROLE_USER)
			              	.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			        	roles.add(userRole);
		        }
	    	});
	    }

	    user.setRole(roles);
	    userRepo.save(user);

	    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	  }
}

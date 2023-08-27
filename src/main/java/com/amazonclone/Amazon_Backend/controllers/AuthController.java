package com.amazonclone.Amazon_Backend.controllers;

import java.util.Collections;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonclone.Amazon_Backend.dto.UserDTO;
import com.amazonclone.Amazon_Backend.exception.UserNotFoundException;
import com.amazonclone.Amazon_Backend.services.UserService;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {

	
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> registerHandler(@Valid @RequestBody UserDTO user) throws UserNotFoundException {
		//String encodedPass = passwordEncoder.encode(user.getPassword());

		//user.setPassword(encodedPass);

		UserDTO userDTO = userService.registerUser(user);

		//String token = jwtUtil.generateToken(userDTO.getEmail());

		return new ResponseEntity<Map<String, Object>>(Collections.singletonMap("message", "registerd"),
				HttpStatus.CREATED);
	}
	
	
//	@PostMapping("/login")
//	public Map<String, Object> loginHandler(@Valid @RequestBody LoginCredentials credentials) {
//
//		UsernamePasswordAuthenticationToken authCredentials = new UsernamePasswordAuthenticationToken(
//				credentials.getEmail(), credentials.getPassword());
//
//		authenticationManager.authenticate(authCredentials);
//
//		String token = jwtUtil.generateToken(credentials.getEmail());
//
//		return Collections.singletonMap("jwt-token", token);
//	}
}

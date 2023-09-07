package com.amazonclone.Amazon_Backend.controllers;

import java.util.List;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonclone.Amazon_Backend.dto.UserDTO;
import com.amazonclone.Amazon_Backend.entities.User;
import com.amazonclone.Amazon_Backend.services.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;





@RestController
@RequestMapping("/api")
@Tag(name = "User")
@CrossOrigin
public class UserController {

	@Autowired
	private UserService userService;	
	
	
//	@GetMapping("/admin/users")
//	public ResponseEntity<List<UserDTO>> getUsers() {
//		
//		List<UserDTO> userResponse = userService.getAllUsers();
//		
//		return new ResponseEntity<List<UserDTO>>(userResponse, HttpStatus.FOUND);
//	}
//	
//	@GetMapping("/admin/user")
//	public ResponseEntity<List<User>> getUser() {
//		
//		List<User> userResponse = userService.getUsers();
//		
//		return new ResponseEntity<List<User>>(userResponse, HttpStatus.FOUND);
//	}
	
	
	@GetMapping("/public/users/{userId}")
	public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
		UserDTO user = userService.getUserById(userId);
		
		return new ResponseEntity<UserDTO>(user, HttpStatus.FOUND);
	}
	
	@PutMapping("/public/users/{userId}")
	public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long userId) {
		UserDTO updatedUser = userService.updateUser(userId, userDTO);
		
		return new ResponseEntity<UserDTO>(updatedUser, HttpStatus.OK);
	}
	
	@DeleteMapping("/admin/users/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
		String status = userService.deleteUser(userId);
		
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}
}

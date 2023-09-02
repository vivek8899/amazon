package com.amazonclone.Amazon_Backend.services;

import java.util.List;


import com.amazonclone.Amazon_Backend.dto.UserDTO;
import com.amazonclone.Amazon_Backend.entities.User;


public interface UserService {
	UserDTO registerUser(UserDTO userDTO);
	List<UserDTO> getAllUsers();
	UserDTO getUserById(Long userId);
	UserDTO updateUser(Long userId, UserDTO userDTO);
	String deleteUser(Long userId);
	
	List<User> getUsers();
}

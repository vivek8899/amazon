package com.amazonclone.Amazon_Backend.services;

import java.util.List;


import com.amazonclone.Amazon_Backend.dto.UserDTO;


public interface UserService {
	UserDTO registerUser(UserDTO userDTO);
	List<UserDTO> getAllUsers();
	UserDTO getUserById(Long userId);
	UserDTO updateUser(Long userId, UserDTO userDTO);
	String deleteUser(Long userId);
}

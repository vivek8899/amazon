package com.amazonclone.Amazon_Backend;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.amazonclone.Amazon_Backend.config.AppConstants;
import com.amazonclone.Amazon_Backend.dto.UserDTO;
import com.amazonclone.Amazon_Backend.entities.Role;
import com.amazonclone.Amazon_Backend.entities.User;
import com.amazonclone.Amazon_Backend.exception.UserNotFoundException;
import com.amazonclone.Amazon_Backend.repository.RoleRepository;
import com.amazonclone.Amazon_Backend.repository.UserRepository;

import jakarta.validation.Valid;

@SpringBootApplication
public class AmazonBackendApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(AmazonBackendApplication.class, args);
	}

}

package com.amazonclone.Amazon_Backend;

import java.util.HashSet;

import java.util.List;
import java.util.Set;

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
import com.amazonclone.Amazon_Backend.services.UserService;

import jakarta.validation.Valid;

@SpringBootApplication
public class AmazonBackendApplication  implements CommandLineRunner {

	@Bean
	public ModelMapper modelMapper() {
		
		return new ModelMapper();
	}
	

	@Autowired
	private RoleRepository roleRepo;
	

	@Autowired
	private UserService userService;
	


	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepo;




	public static void main(String[] args) {
		SpringApplication.run(AmazonBackendApplication.class, args);
		
		
	}

	@Override
	public void run(String... args) throws Exception {
		

		try {
		
			Role adminRole = new Role();
			adminRole.setRoleId(AppConstants.ADMIN_ID);
			adminRole.setRoleName("ADMIN");

			Role userRole = new Role();
			userRole.setRoleId(AppConstants.USER_ID);
			userRole.setRoleName("USER");
			List<Role> roles = List.of(adminRole, userRole);

			roleRepo.saveAll(roles);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// = Only When if the User Table Is Empty
				// ==============================================================
				if (userService.getUsers().size() == 0) {
					
					User user = new User();
					user.setFirstName("admin");
					user.setLastName("admin");
					user.setEmail("admin@gmail.com");
					user.setMobileNumber("9876543210");
					user.setPassword( passwordEncoder.encode("admin"));
					
					
					userRepo.save(user);
					Role adminRole = new Role();
					adminRole.setRoleId(AppConstants.ADMIN_ID);
					adminRole.setRoleName("ADMIN");
					Set<Role> rl = new HashSet<>();
					rl.add(adminRole);
					user.setRoles(rl);
					user.setAddresses(null);
					user.setCart(null);
					userRepo.save(user);


				}

	}

}

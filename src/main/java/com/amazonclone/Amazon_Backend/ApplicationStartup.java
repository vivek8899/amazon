package com.amazonclone.Amazon_Backend;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.amazonclone.Amazon_Backend.config.AppConstants;
import com.amazonclone.Amazon_Backend.entities.Role;
import com.amazonclone.Amazon_Backend.entities.User;
import com.amazonclone.Amazon_Backend.repository.RoleRepository;
import com.amazonclone.Amazon_Backend.repository.UserRepository;
import com.amazonclone.Amazon_Backend.services.UserService;


@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepo;

// ==============================================================
	// = onLoad The Admin And LibrarianS
// ==============================================================
	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		initDatabaseEntities();
	}


	private void initDatabaseEntities() {
		
		
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

		// ==============================================================
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
	

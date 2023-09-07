package com.amazonclone.Amazon_Backend.services;

import java.util.List;





import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.amazonclone.Amazon_Backend.config.AppConstants;
import com.amazonclone.Amazon_Backend.dto.AddressDTO;
import com.amazonclone.Amazon_Backend.dto.CartDTO;
import com.amazonclone.Amazon_Backend.dto.UserDTO;

import com.amazonclone.Amazon_Backend.dto.ProductDTO;
import com.amazonclone.Amazon_Backend.entities.Address;
import com.amazonclone.Amazon_Backend.entities.Cart;
import com.amazonclone.Amazon_Backend.entities.CartItem;
import com.amazonclone.Amazon_Backend.entities.Role;
import com.amazonclone.Amazon_Backend.entities.User;
import com.amazonclone.Amazon_Backend.exception.APIException;
import com.amazonclone.Amazon_Backend.exception.ResourceNotFoundException;
import com.amazonclone.Amazon_Backend.repository.AddressRepository;
import com.amazonclone.Amazon_Backend.repository.RoleRepository;
import com.amazonclone.Amazon_Backend.repository.UserRepository;


import jakarta.transaction.Transactional;



@Transactional
@Service
public class UserServiceImpl implements UserService {

	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private AddressRepository addressRepo;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private ModelMapper modelMapper;


	@Override
	public UserDTO registerUser(UserDTO userDTO) {
		try {
			User user = modelMapper.map(userDTO, User.class);

			Cart cart = new Cart();
			user.setCart(cart);

			Role role = roleRepo.findById(AppConstants.USER_ID).get();
			user.getRoles().add(role);

			String country = "Default";
			String state = "Default";
			String city = "Default";
			String pincode = "000000";
			String street = "default";
			String buildingName = "default";

			Address addressFromDB = addressRepo.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(country,
					state, city, pincode, street, buildingName);
			if(addressFromDB == null) {
				
				Address address = new Address(country, state, city, pincode, street, buildingName);
				addressFromDB = addressRepo.save(address);
			}
			
		
			user.setAddresses(List.of(addressFromDB));

			User registeredUser = userRepo.save(user);

			cart.setUser(registeredUser);

			userDTO = modelMapper.map(registeredUser, UserDTO.class);

			userDTO.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));

			return userDTO;
		} catch (DataIntegrityViolationException e) {
			throw new APIException("User already exists with emailId: " + userDTO.getEmail());
		}

	}


	
	
	
	
	@Override
	public List<UserDTO> getAllUsers() {
		List<User> users = userRepo.findAll();
	
		List<UserDTO> userDTOs = users.stream().map(user -> {
			UserDTO dto = modelMapper.map(user, UserDTO.class);

			if (user.getAddresses().size() != 0) {
				dto.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));
			}

			CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);

			List<ProductDTO> products = user.getCart().getCartItems().stream()
					.map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).collect(Collectors.toList());

			dto.setCart(cart);

			dto.getCart().setProducts(products);

			return dto;

		}).collect(Collectors.toList());
		
	
	
		
		return userDTOs;
	}


	@Override
	public UserDTO getUserById(Long userId) {
		
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		UserDTO userDTO = modelMapper.map(user, UserDTO.class);
		
		userDTO.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));

		CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);
	
		List<ProductDTO> products = user.getCart().getCartItems().stream()
				.map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).collect(Collectors.toList());

		userDTO.setCart(cart);

		userDTO.getCart().setProducts(products);

		return userDTO;
	
	
	}


	@Override
	public UserDTO updateUser(Long userId, UserDTO userDTO) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
	
		//String encodedPass = passwordEncoder.encode(userDTO.getPassword());

		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		
		user.setEmail(userDTO.getEmail());
		//user.setPassword(encodedPass);

		if (userDTO.getAddress() != null) {
			String country = userDTO.getAddress().getCountry();
			String state = userDTO.getAddress().getState();
			String city = userDTO.getAddress().getCity();
			String pincode = userDTO.getAddress().getPincode();
			String street = userDTO.getAddress().getStreet();
			String buildingName = userDTO.getAddress().getBuildingName();

			Address address = addressRepo.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(country, state,
					city, pincode, street, buildingName);

			if (address == null) {
				address = new Address(country, state, city, pincode, street, buildingName);

				address = addressRepo.save(address);

				user.setAddresses(List.of(address));
			}
		}

		userDTO = modelMapper.map(user, UserDTO.class);

		userDTO.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));

		CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);

		List<ProductDTO> products = user.getCart().getCartItems().stream()
				.map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).collect(Collectors.toList());

		userDTO.setCart(cart);

		userDTO.getCart().setProducts(products);

		return userDTO;
	
	
	}


	@Override
	public String deleteUser(Long userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
	
		List<CartItem> cartItems = user.getCart().getCartItems();
		Long cartId = user.getCart().getCartId();
	
		cartItems.forEach(item -> {

			Long productId = item.getProduct().getProductId();

			cartService.deleteProductFromCart(cartId, productId);
		});

		userRepo.delete(user);

		return "User with userId " + userId + " deleted successfully!!!";
	
	}






	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return userRepo.findAll();
	}

	

}

package com.amazonclone.Amazon_Backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonclone.Amazon_Backend.dto.AddressDTO;
import com.amazonclone.Amazon_Backend.entities.Address;
import com.amazonclone.Amazon_Backend.entities.User;
import com.amazonclone.Amazon_Backend.exception.APIException;
import com.amazonclone.Amazon_Backend.exception.ResourceNotFoundException;
import com.amazonclone.Amazon_Backend.repository.AddressRepository;
import com.amazonclone.Amazon_Backend.repository.UserRepository;

import jakarta.transaction.Transactional;




@Transactional
@Service
public class AddressServiceImpl implements AddressService{

	
	
	@Autowired
	private AddressRepository addressRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public AddressDTO createAddress(AddressDTO addressDTO) {
		String country = addressDTO.getCountry();
		String state = addressDTO.getState();
		String city = addressDTO.getCity();
		String pincode = addressDTO.getPincode();
		String street = addressDTO.getStreet();
		String buildingName = addressDTO.getBuildingName();

		Address addressFromDB = addressRepo.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(country,
				state, city, pincode, street, buildingName);

		if (addressFromDB != null) {
			throw new APIException("Address already exists with addressId: " + addressFromDB.getAddressId());
		}

		Address address = modelMapper.map(addressDTO, Address.class);

		Address savedAddress = addressRepo.save(address);

		return modelMapper.map(savedAddress, AddressDTO.class);
	}

	@Override
	public List<AddressDTO> getAddresses() {
		List<Address> addresses = addressRepo.findAll();

		List<AddressDTO> addressDTOs = addresses.stream().map(address -> modelMapper.map(address, AddressDTO.class))
				.collect(Collectors.toList());

		return addressDTOs;
	}

	@Override
	public AddressDTO getAddress(Long addressId) {
		Address address = addressRepo.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

		return modelMapper.map(address, AddressDTO.class);
	}

	@Override
	public AddressDTO updateAddress(Long addressId, Address address) {
		Address addressFromDB = addressRepo.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(
				address.getCountry(), address.getState(), address.getCity(), address.getPincode(), address.getStreet(),
				address.getBuildingName());

		if (addressFromDB == null) {
			addressFromDB = addressRepo.findById(addressId)
					.orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

			addressFromDB.setCountry(address.getCountry());
			addressFromDB.setState(address.getState());
			addressFromDB.setCity(address.getCity());
			addressFromDB.setPincode(address.getPincode());
			addressFromDB.setStreet(address.getStreet());
			addressFromDB.setBuildingName(address.getBuildingName());

			Address updatedAddress = addressRepo.save(addressFromDB);

			return modelMapper.map(updatedAddress, AddressDTO.class);
		} else {
			List<User> users = userRepo.findByAddress(addressId);
			final Address a = addressFromDB;

			users.forEach(user -> user.getAddresses().add(a));

			deleteAddress(addressId);

			return modelMapper.map(addressFromDB, AddressDTO.class);
		}
	}
	

	@Override
	public String deleteAddress(Long addressId) {
		Address addressFromDB = addressRepo.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

		List<User> users = userRepo.findByAddress(addressId);

		users.forEach(user -> {
			user.getAddresses().remove(addressFromDB);

			userRepo.save(user);
		});

		addressRepo.deleteById(addressId);

		return "Address deleted succesfully with addressId: " + addressId;
	}

	@Override
	public AddressDTO createAddressForUser(AddressDTO addressDTO, Long id) {
		
		
		User user = userRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", id));
		String country = addressDTO.getCountry();
		String state = addressDTO.getState();
		String city = addressDTO.getCity();
		String pincode = addressDTO.getPincode();
		String street = addressDTO.getStreet();
		String buildingName = addressDTO.getBuildingName();

		Address addressFromDB = addressRepo.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(country,
				state, city, pincode, street, buildingName);

		if (addressFromDB != null) {
			throw new APIException("Address already exists with addressId: " + addressFromDB.getAddressId());
		}

		Address address = modelMapper.map(addressDTO, Address.class);
	

		Address savedAddress = addressRepo.save(address);
		List<Address> address1 = user.getAddresses();
		address1.add(savedAddress);
		
		user.setAddresses(address1);
		
		userRepo.save(user);

		return modelMapper.map(savedAddress, AddressDTO.class);
		
	}


}

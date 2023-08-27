package com.amazonclone.Amazon_Backend.services;

import java.util.List;

import com.amazonclone.Amazon_Backend.dto.AddressDTO;
import com.amazonclone.Amazon_Backend.entities.Address;



public interface AddressService {

	AddressDTO createAddress( AddressDTO addressDTO);
	
	AddressDTO createAddressForUser(AddressDTO addressDTO, Long id);

	List<AddressDTO> getAddresses();

	AddressDTO getAddress(Long addressId);

	AddressDTO updateAddress(Long addressId, Address address);

	String deleteAddress(Long addressId);

}

package com.amazonclone.Amazon_Backend.controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonclone.Amazon_Backend.dto.AddressDTO;
import com.amazonclone.Amazon_Backend.entities.Address;
import com.amazonclone.Amazon_Backend.services.AddressService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api")
@Tag(name = "Address")
@CrossOrigin
public class AddressController {

	
	@Autowired
	private AddressService addressService;
	
	@PostMapping("public/address/{userId}")
	public ResponseEntity<AddressDTO> addUserAddress(@Valid @RequestBody AddressDTO addressDTO,
			@PathVariable ("userId") Long userId) {
		AddressDTO savedAddressDTO = addressService.createAddressForUser(addressDTO, userId);
		
		return new ResponseEntity<AddressDTO>(savedAddressDTO, HttpStatus.CREATED);
	}
	
	
	
	@PostMapping("admin/address")
	public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
		AddressDTO savedAddressDTO = addressService.createAddress(addressDTO);
		
		return new ResponseEntity<AddressDTO>(savedAddressDTO, HttpStatus.CREATED);
	}
	
	@GetMapping("admin/addresses")
	public ResponseEntity<List<AddressDTO>> getAddresses() {
		List<AddressDTO> addressDTOs = addressService.getAddresses();
		
		return new ResponseEntity<List<AddressDTO>>(addressDTOs, HttpStatus.FOUND);
	}
	
	@GetMapping("admin/addresses/{addressId}")
	public ResponseEntity<AddressDTO> getAddress(@PathVariable Long addressId) {
		AddressDTO addressDTO = addressService.getAddress(addressId);
		
		return new ResponseEntity<AddressDTO>(addressDTO, HttpStatus.FOUND);
	}
	
	@PutMapping("public/addresses/{addressId}")
	public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId, @RequestBody Address address) {
		AddressDTO addressDTO = addressService.updateAddress(addressId, address);
		
		return new ResponseEntity<AddressDTO>(addressDTO, HttpStatus.OK);
	}
	
	@DeleteMapping("admin/addresses/{addressId}")
	public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
		String status = addressService.deleteAddress(addressId);
		
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}
	
}

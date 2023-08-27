package com.amazonclone.Amazon_Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amazonclone.Amazon_Backend.entities.Address;


@Repository
public interface AddressRepository  extends JpaRepository<Address, Long>{

	Address findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(String country, String state, String city,
			String pincode, String street, String buildingName);

}

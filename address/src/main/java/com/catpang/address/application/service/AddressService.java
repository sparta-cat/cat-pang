package com.catpang.address.application.service;

import com.catpang.address.domain.model.Address;
import com.catpang.address.domain.repository.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AddressService {

	private final AddressRepository addressRepository;

	public AddressService(AddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}

	// Address ID가 UUID 타입으로 변경됨
	public Address getAddressById(UUID id) {
		return addressRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Address not found: " + id));
	}

	public List<Address> getAllAddresses() {
		return addressRepository.findAll();
	}

	@Transactional
	public Address createAddress(Address address) {
		return addressRepository.save(address);
	}
}

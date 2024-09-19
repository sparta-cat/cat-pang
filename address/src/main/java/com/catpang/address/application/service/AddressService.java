package com.catpang.address.application.service;

import com.catpang.address.application.dto.AddressDto;
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

	// Address ID로 AddressDto를 반환하는 메서드 추가
	public AddressDto.Result getAddressById(UUID id) {
		Address address = addressRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Address not found: " + id));
		return toDto(address);
	}

	// Address 엔티티를 AddressDto로 변환하는 메서드
	private AddressDto.Result toDto(Address address) {
		return AddressDto.Result.builder()
			.id(address.getId())
			.city(address.getCity())
			.street(address.getStreet())
			.zipcode(address.getZipCode())
			.latitude(address.getLatitude())
			.longitude(address.getLongitude())
			.build();
	}

	// 나머지 메서드들 (이미 정의된 것)
	public List<Address> getAllAddresses() {
		return addressRepository.findAll();
	}

	@Transactional
	public Address createAddress(Address address) {
		return addressRepository.save(address);
	}
}

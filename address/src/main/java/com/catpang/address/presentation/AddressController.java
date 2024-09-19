package com.catpang.address.presentation;

import com.catpang.address.domain.model.Address;
import com.catpang.address.application.service.AddressService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/addresses")
@Validated
public class AddressController {

	private final AddressService addressService;

	public AddressController(AddressService addressService) {
		this.addressService = addressService;
	}

	// UUID 타입으로 변경
	@GetMapping("/{id}")
	public Address getAddressById(@PathVariable UUID id) {
		return addressService.getAddressById(id);
	}

	@GetMapping
	public List<Address> getAllAddresses() {
		return addressService.getAllAddresses();
	}

	@PostMapping
	public Address createAddress(@RequestBody Address address) {
		return addressService.createAddress(address);
	}
}

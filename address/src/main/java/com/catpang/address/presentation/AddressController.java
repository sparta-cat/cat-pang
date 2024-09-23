package com.catpang.address.presentation;

import com.catpang.address.application.dto.AddressDto;
import com.catpang.address.application.service.AddressService;
import com.catpang.address.domain.model.Address;
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

	// AddressDto.Result 타입을 반환하도록 수정
	@GetMapping("/{id}")
	public AddressDto.Result getAddressById(@PathVariable UUID id) {
		return addressService.getAddressById(id);
	}

	@GetMapping
	public List<Address> getAllAddresses() {
		return addressService.getAllAddresses();
	}

	@PostMapping
	public Address createAddress(@RequestBody AddressDto.Create address) {
		return addressService.createAddress(address);
	}
}
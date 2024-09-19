package com.catpang.address.presentation.internal;

import com.catpang.address.application.dto.AddressDto;
import com.catpang.address.application.service.AddressService;
import com.catpang.core.application.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/internal/addresses")
@RequiredArgsConstructor
public class AddressInternalController {

	private final AddressService addressService;

	@GetMapping("/{id}")
	public ApiResponse<AddressDto.Result> getAddressById(@PathVariable UUID id) {
		AddressDto.Result address = addressService.getAddressById(id);
		return ApiResponse.Success.<AddressDto.Result>builder()
			.result(address)
			.build();
	}
}

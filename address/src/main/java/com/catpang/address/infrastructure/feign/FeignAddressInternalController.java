package com.catpang.address.infrastructure.feign;

import com.catpang.core.application.dto.AddressDto;
import com.catpang.core.application.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "address-service")
public interface FeignAddressInternalController {

	@GetMapping("/api/v1/internal/addresses/{id}")
	ApiResponse<AddressDto.Result> getAddress(@PathVariable UUID id);
}

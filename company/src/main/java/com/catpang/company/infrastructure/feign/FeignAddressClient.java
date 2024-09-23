package com.catpang.company.infrastructure.feign;

import com.catpang.address.domain.model.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "address-service")
public interface FeignAddressClient {

	@GetMapping("/api/v1/internal/addresses/{id}")
	Address getAddress(@PathVariable UUID id);
}

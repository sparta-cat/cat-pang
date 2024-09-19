package com.catpang.company.infrastructure.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "address-service")
public interface FeignAddressClient {

	@GetMapping("/api/v1/addresses/{id}")
	void getAddress(@PathVariable UUID id);
}

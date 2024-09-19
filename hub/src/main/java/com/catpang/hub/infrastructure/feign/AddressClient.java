package com.catpang.hub.infrastructure.feign;

import com.catpang.address.domain.model.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "address-service")
public interface AddressClient {

	@GetMapping("/api/v1/addresses/{id}")
	Address getAddressById(@PathVariable("id") Long id);
}

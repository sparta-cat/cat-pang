package com.catpang.hubproduct.infrastructure.feign;

import com.catpang.core.application.dto.HubDto;
import com.catpang.core.application.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub-service")
public interface FeignHubInternalController {

	@GetMapping("/api/v1/hubs/{id}")
	ApiResponse<HubDto.Result> getHub(@PathVariable UUID id);
}
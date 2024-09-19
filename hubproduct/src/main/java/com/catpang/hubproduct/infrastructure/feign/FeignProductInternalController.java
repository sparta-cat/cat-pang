package com.catpang.hubproduct.infrastructure.feign;

import com.catpang.core.application.dto.ProductDto;
import com.catpang.core.application.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "product-service")
public interface FeignProductInternalController {

	@GetMapping("/api/v1/products/{id}")
	ApiResponse<ProductDto.Result> getProduct(@PathVariable UUID id);
}